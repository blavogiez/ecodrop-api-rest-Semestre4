package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AcceptsDAO;
import model.dao.AcceptsDAOPostgres;
import model.dto.Accepts;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/accepts/*")
public class AcceptsRestAPI extends HttpServlet {
    AcceptsDAO dao = new AcceptsDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.segments.length != 0) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Collection<Accepts> l = dao.findAll();
        ctx.out.print(ctx.mapper.writeValueAsString(l));
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);
        try {
            String data = RequestUtils.readBody(req);
            Accepts accepts = ctx.mapper.readValue(data, Accepts.class);
            System.out.println(accepts);

            if (!dao.add(accepts)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            ctx.out.print(ctx.mapper.writeValueAsString(accepts));
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println(e);
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.segments.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int pointId = RequestUtils.parseId(ctx.segments[0], res);
        if (pointId < 0) return;
        int wasteTypeId = RequestUtils.parseId(ctx.segments[1], res);
        if (wasteTypeId < 0) return;

        if (!dao.delete(pointId, wasteTypeId)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
