package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CollectionPointDAO;
import model.dao.CollectionPointDAOPostgres;
import model.dao.DepositDAO;
import model.dao.DepositDAOPostgres;
import model.dto.CollectionPointStatus;
import model.dto.Deposit;
import model.dto.DepositView;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/deposits/*")
public class DepositRestAPI extends HttpServlet {

    DepositDAO dao = new DepositDAOPostgres();
    CollectionPointDAO pointDao = new CollectionPointDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (!ctx.hasArguments()) {
            Collection<DepositView> deposits = dao.findAllEnriched();
            ctx.printValueAsString(deposits);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        Deposit deposit = dao.findById(id);
        if (deposit == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.printValueAsString(deposit);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);
        try {
            String data = RequestUtils.readBody(req);
            Deposit deposit = ctx.readValue(data, Deposit.class);
            System.out.println(deposit);

            if (deposit.getPoids() < 0) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (!ctx.isAuthenticated()) {
                res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            CollectionPointStatus status = pointDao.getStatus(deposit.getPointId());
            if (status == null || status.taux >= 100.0) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            if (!dao.add(deposit)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            ctx.printValueAsString(deposit);
            res.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            System.out.println("Could not add deposit to database : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
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

        if (!ctx.isAuthenticated()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String data = RequestUtils.readBody(req);

            Deposit existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Deposit merged = ctx.readerForUpdating(existing).readValue(data);
            Deposit updated = dao.update(id, merged);
            if (updated == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ctx.printValueAsString(updated);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not patch deposit : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (!ctx.isAuthenticated()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String data = RequestUtils.readBody(req);

        Deposit updatedDeposit = ctx.readValue(data, Deposit.class);
        Deposit deposit = dao.update(id, updatedDeposit);

        if (deposit == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.printValueAsString(deposit);
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
