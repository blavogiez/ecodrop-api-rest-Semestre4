package controller;

import java.io.IOException;
import java.util.Collection;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.WasteTypeDAO;
import model.dao.WasteTypeDAOPostgres;
import model.dto.WasteType;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/waste-types/*")
public class WasteTypeRestAPI extends HttpServlet {

    WasteTypeDAO dao = new WasteTypeDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.segments.length == 0) {
            Collection<WasteType> lesWasteTypes = dao.findAll();
            ctx.out.print(ctx.mapper.writeValueAsString(lesWasteTypes));
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (ctx.segments.length != 1) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.segments[0], res);
        if (id < 0) return;
        WasteType wasteType = dao.findById(id);
        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.out.print(ctx.mapper.writeValueAsString(wasteType));
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);
        try {
            String data = RequestUtils.readBody(req);
            WasteType wasteType = ctx.mapper.readValue(data, WasteType.class);
            System.out.println(wasteType);

            if (!dao.add(wasteType)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }
            ctx.out.print(ctx.mapper.writeValueAsString(wasteType));
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Could not add waste type to database : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.segments.length != 1) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.segments[0], res);
        if (id < 0) return;

        if (!dao.delete(id)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        res.setStatus(HttpServletResponse.SC_OK);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.segments.length != 1) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.segments[0], res);
        if (id < 0) return;
        String data = RequestUtils.readBody(req);

        WasteType updatedWasteType = ctx.mapper.readValue(data, WasteType.class);
        WasteType wasteType = dao.update(id, updatedWasteType);

        if (wasteType == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.out.print(ctx.mapper.writeValueAsString(wasteType));
        res.setStatus(HttpServletResponse.SC_OK);
    }
}
