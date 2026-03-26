package controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.UsersDAO;
import model.dao.UsersDAOPostgres;
import model.dto.Users;
import utils.RequestContext;
import utils.RequestUtils;

@WebServlet("/users/*")
public class UsersRestAPI extends HttpServlet {

    UsersDAO dao = new UsersDAOPostgres();

    private static final int DEFAULT_LEADERBOARD_LIMIT = 10;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (!ctx.hasArguments()) {
            Collection<Users> users = dao.findAll();
            ctx.printValueAsString(users);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (ctx.getArgument(0).equals("leaderboard")) {
            List<Users> theTenBestRecyclers = dao.findArgumentTopRecyclers(DEFAULT_LEADERBOARD_LIMIT);
            ctx.printValueAsString(theTenBestRecyclers);
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        res.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (RequestUtils.tokenIsInvalid(ctx, req)){
            res.sendError(RequestUtils.getTokenError(ctx, req));
            return;
        }

        String data = RequestUtils.readBody(req);

        Users updatedUser = ctx.readValue(data, Users.class);
        Users user = dao.update(id, updatedUser);

        if (user == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        ctx.printValueAsString(user);
        res.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPatch(HttpServletRequest req, HttpServletResponse res) throws IOException {
        RequestContext ctx = new RequestContext(req, res);

        if (ctx.doesNotHaveExactlyXArguments(1)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = RequestUtils.parseId(ctx.getArgument(0), res);
        if (id < 0) return;

        if (RequestUtils.tokenIsInvalid(ctx, req)){
            res.sendError(RequestUtils.getTokenError(ctx, req));
            return;
        }

        try {
            String data = RequestUtils.readBody(req);

            Users existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Users updated = ctx.readerForUpdating(existing).readValue(data);
            if (dao.update(id, updated) == null) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            ctx.printValueAsString(updated);
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not update user : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
