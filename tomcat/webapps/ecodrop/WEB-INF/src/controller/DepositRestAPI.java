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
import model.dao.DepositDAO;
import model.dao.DepositDAOPostgres;
import model.dto.Deposit;
import utils.FormatAdapter;

@WebServlet("/deposit/*")
public class DepositRestAPI extends HttpServlet {

    DepositDAO dao = new DepositDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType(FormatAdapter.contentTypeFor(req));
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<Deposit> deposits = dao.findAll();
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

            if (!dao.add(deposit)) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            } else {
                System.out.println("IL PASSE");
                PrintWriter out = res.getWriter();
                out.print(objectMapper.writeValueAsString(deposit));
                res.setStatus(HttpServletResponse.SC_OK);
            }

        } catch (Exception e) {
            System.out.println("Could not add deposit to database : " + e.getMessage());
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
