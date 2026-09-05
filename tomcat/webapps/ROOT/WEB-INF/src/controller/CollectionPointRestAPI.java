package controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import model.dao.CollectionPointDAO;
import model.dao.CollectionPointDAOPostgres;
import model.dto.CollectionPoint;
import model.dto.CollectionPointStatus;
import model.dto.Deposit;
import model.dto.WasteType;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/points/*")
public class CollectionPointRestAPI extends HttpServlet {

    public static final int OVERLOADED_THRESHOLD = 80;

    CollectionPointDAO dao = new CollectionPointDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (!ctx.hasArguments()) {
            Collection<CollectionPoint> l = dao.findAll();
            ctx.printValueAsString(l);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (!ctx.doesNotHaveExactlyXArguments(2) && ctx.getArgument(1).equals("status")) {
            int id = RequestUtils.parseId(ctx.getArgument(0), res);
            if (id < 0) return;
            CollectionPointStatus status = dao.getStatus(id);
            if (status == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            ctx.printValueAsString(status);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (ctx.getArgument(0).equals("overloaded")) {
            if (!ctx.hasRole(Role.ADMIN)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
            List<CollectionPoint> pointsAboveThreshold = dao.getOccupatedPointsAboveThreshold(OVERLOADED_THRESHOLD);
            ctx.printValueAsString(pointsAboveThreshold);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;
        CollectionPoint collectionPoint = dao.findById(id);
        if (collectionPoint == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        List<WasteType> lesWasteTypesAcceptes = dao.getAcceptedWasteTypes(id);
        collectionPoint.setWasteTypes(lesWasteTypesAcceptes);
        ctx.printValueAsString(collectionPoint);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (!ctx.hasRole(Role.USER)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        try {
            String data = RequestUtils.readBody(req);

            CollectionPoint existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            CollectionPoint toPut = ctx.readValue(data, CollectionPoint.class);
            if (dao.update(toPut) == null) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            ctx.printValueAsString(toPut);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not update collection point : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (!ctx.hasRole(Role.USER)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String data = RequestUtils.readBody(req);

            // recup l'objet existant pour ne modifier que les champs fournis
            CollectionPoint existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            CollectionPoint updated = ctx.readerForUpdating(existing).readValue(data);
            if (dao.update(updated) == null) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            ctx.printValueAsString(updated);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not update collection point : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(2)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (!ctx.hasRole(Role.ADMIN)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (ctx.getArgument(1).equals("clear")) {
            List<Deposit> deletedDeposits = dao.deleteAllDepositsFromPoint(id);
            ctx.printValueAsString(deletedDeposits);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
