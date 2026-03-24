package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
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
import utils.FormatAdapter;

@WebServlet("/deposit/*")
public class DepositRestAPI extends HttpServlet {

    DepositDAO dao = new DepositDAOPostgres();
    CollectionPointDAO pointDao = new CollectionPointDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<DepositView> deposits = dao.findAllEnriched();
            out.print(objectMapper.writeValueAsString(deposits));
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        Deposit deposit = dao.findById(Integer.parseInt(id));
        if (deposit == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(deposit));
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).lines()
                .collect(Collectors.joining());

        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        try {
            Deposit deposit = objectMapper.readValue(data, Deposit.class);
            System.out.println(deposit);

            if (deposit.getPoids() < 0) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
            } else {
                PrintWriter out = res.getWriter();
                out.print(objectMapper.writeValueAsString(deposit));
                res.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            System.out.println("Could not add deposit to database : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        String info = req.getPathInfo();
        String[] splits = info == null ? new String[0] : info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        int id = Integer.parseInt(splits[1]);

        try {
            String data = new BufferedReader(new InputStreamReader(req.getInputStream())).lines()
                    .collect(Collectors.joining());

            Deposit existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
            Deposit merged = objectMapper.readerForUpdating(existing).readValue(data);

            Deposit updated = dao.update(id, merged);
            if (updated == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            PrintWriter out = res.getWriter();
            out.print(objectMapper.writeValueAsString(updated));
            res.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not patch deposit : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        String data = new BufferedReader(new InputStreamReader(req.getInputStream())).lines()
                .collect(Collectors.joining());

        Deposit updatedDeposit = objectMapper.readValue(data, Deposit.class);

        Deposit deposit = dao.update(Integer.parseInt(id), updatedDeposit);

        if (deposit == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(deposit));
        res.setStatus(HttpServletResponse.SC_OK);
    }

}
