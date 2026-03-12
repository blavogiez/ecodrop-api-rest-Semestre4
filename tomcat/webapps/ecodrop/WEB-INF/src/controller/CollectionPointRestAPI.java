package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.CollectionPointDAO;
import model.dao.CollectionPointDAOPostgres;
import model.dto.CollectionPoint;
import model.dto.WasteType;
import utils.FormatAdapter;

@WebServlet("/points/*")
public class CollectionPointRestAPI extends HttpServlet {

    CollectionPointDAO dao = new CollectionPointDAOPostgres();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        PrintWriter out = res.getWriter();
        ObjectMapper objectMapper = FormatAdapter.mapperFor(req);
        String info = req.getPathInfo();

        System.out.println(info);

        if (info == null || info.equals("/")) {
            Collection<CollectionPoint> l = dao.findAll();
            String jsonstring = objectMapper.writeValueAsString(l);
            out.print(jsonstring);
            return;
        }
        String[] splits = info.split("/");
        if (splits.length != 2) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        List<WasteType> lesWasteTypesAcceptes = dao.getAcceptedWasteTypes(Integer.parseInt(id));
        System.out.println(lesWasteTypesAcceptes);
        if (lesWasteTypesAcceptes == null) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        out.print(objectMapper.writeValueAsString(lesWasteTypesAcceptes));
        return;
    }

    // doPut à faire

    public void doPatch(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");

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

            // recup l'objet existant pour ne modifier que les champs fournis
            CollectionPoint existing = dao.findById(id);
            if (existing == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ObjectMapper mapper = FormatAdapter.mapperFor(req);
            CollectionPoint updated = mapper.readerForUpdating(existing).readValue(data);

            if (dao.update(updated) == null) {
                res.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            PrintWriter out = res.getWriter();
            out.print(mapper.writeValueAsString(updated));
            res.sendError(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.out.println("Could not update collection point : " + e.getMessage());
            res.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/json;charset=UTF-8");
        String info = req.getPathInfo();

        System.out.println(info);

        String[] splits = info.split("/");
        if (splits.length != 3) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String id = splits[1];
        String order = splits[2];

        if (order.equals("clear")) {
            boolean success = dao.delete(Integer.parseInt(id));

            if (!success) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            res.sendError(HttpServletResponse.SC_OK);
            return;
        }
        res.sendError(HttpServletResponse.SC_BAD_REQUEST);
        return;
    }
}
