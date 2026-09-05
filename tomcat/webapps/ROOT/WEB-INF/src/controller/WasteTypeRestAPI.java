package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import model.dao.WasteTypeDAO;
import model.dao.WasteTypeDAOPostgres;
import model.dto.WasteType;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/waste-types/*")
public class WasteTypeRestAPI extends HttpServlet {

    WasteTypeDAO dao = new WasteTypeDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (!ctx.hasArguments()) {
            Collection<WasteType> lesWasteTypes = dao.findAll();
            ctx.printValueAsString(lesWasteTypes);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;
        WasteType wasteType = dao.findById(id);
        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.printValueAsString(wasteType);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);
        try {
            if (!ctx.hasRole(Role.USER)) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String data = RequestUtils.readBody(req);
            WasteType wasteType = ctx.readValue(data, WasteType.class);
            System.out.println(wasteType);

            if (!dao.add(wasteType)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            ctx.printValueAsString(wasteType);
            res.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            System.err.println("Could not add waste type to database : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (!ctx.hasRole(Role.ADMIN)) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (dao.isReferencedInDeposits(id)) {
            res.sendError(HttpServletResponse.SC_CONFLICT);
            return;
        }
        if (!dao.delete(id)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
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

        String data = RequestUtils.readBody(req);

        WasteType updatedWasteType = ctx.readValue(data, WasteType.class);
        WasteType wasteType = dao.update(id, updatedWasteType);

        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.printValueAsString(wasteType);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
