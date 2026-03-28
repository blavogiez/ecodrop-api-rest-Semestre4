package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Role;
import model.dao.AcceptsDAO;
import model.dao.AcceptsDAOPostgres;
import model.dto.Accepts;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/accepts/*")
public class AcceptsRestAPI extends HttpServlet {
    AcceptsDAO dao = new AcceptsDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.hasArguments()) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Collection<Accepts> l = dao.findAll();
        ctx.printValueAsString(l);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (!ctx.isAuthenticated()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String data = RequestUtils.readBody(req);
            Accepts accepts = ctx.readValue(data, Accepts.class);

            if (!dao.add(accepts)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            ctx.printValueAsString(accepts);
            res.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            System.out.println("Could not update Accepts : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(2)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int pointId = RequestUtils.parseId(ctx.getArgument(0), res);
        if (pointId < 0) return;
        int wasteTypeId = RequestUtils.parseId(ctx.getArgument(1), res);
        if (wasteTypeId < 0) return;

        if (ctx.getUserRole() != Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!dao.delete(pointId, wasteTypeId)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
