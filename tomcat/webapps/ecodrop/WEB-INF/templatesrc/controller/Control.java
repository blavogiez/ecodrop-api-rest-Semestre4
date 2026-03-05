package controller;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.JoueurDao;
import model.JoueurDaoJdbc;
import model.Partie;
import model.PartieDao;
import model.PartieDaoJdbc;

@WebServlet("/Control")
public class Control extends HttpServlet {

    // GET = afficher une page / un formulaire (pas de traitement)
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) {
            action = "voir";
        }

        String vue;
        PartieDao partieDao;
        JoueurDao joueurDao;

        switch (action) {
            case "voir":
                vue = "WEB-INF/view/view.jsp";
                break;

            case "list":
                vue = "WEB-INF/view/lister.jsp";
                partieDao = new PartieDaoJdbc();
                req.setAttribute("parties", partieDao.findAll());
                break;

            case "modifier":
                vue = "WEB-INF/view/edit.jsp";
                joueurDao = new JoueurDaoJdbc();
                req.setAttribute("joueurs", joueurDao.findAll());
                break;

            case "delete":
                vue = "WEB-INF/view/delete.jsp";
                break;

            default:
                res.sendError(404, "Action non supportée");
                return;
        }

        req.getRequestDispatcher(vue).forward(req, res);
    }

    // POST = traiter les données d'un formulaire, puis rediriger
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null)
            action = "list";

        PartieDao partieDao;

        switch (action) {
            case "voir":
                partieDao = new PartieDaoJdbc();
                Partie p = partieDao.findById(Integer.parseInt(req.getParameter("n")));
                req.setAttribute("partie", p);
                req.getRequestDispatcher("WEB-INF/view/view.jsp").forward(req, res);
                return;

            case "modifier":
                int pno = Integer.parseInt(req.getParameter("pno"));
                String date = req.getParameter("date");
                String statut = req.getParameter("statut");
                int jno1 = Integer.parseInt(req.getParameter("jno1"));
                int jno2 = Integer.parseInt(req.getParameter("jno2"));

                partieDao = new PartieDaoJdbc();
                Partie partie = partieDao.findById(pno);
                if (partie != null) {
                    partie.setDate(date);
                    partie.setStatut(statut);
                    partie.setJno1(jno1);
                    partie.setJno2(jno2);
                    partieDao.delete(pno);
                    partieDao.create(partie);
                }
                // Après modification, on redirige vers la liste
                res.sendRedirect("Control?action=list");
                return;

            case "delete":
                int deletePno = Integer.parseInt(req.getParameter("pno"));
                partieDao = new PartieDaoJdbc();
                partieDao.delete(deletePno);
                // Après suppression, on redirige vers la liste
                res.sendRedirect("Control?action=list");
                return;

            default:
                res.sendRedirect("Control?action=list");
                return;
        }
    }
}
