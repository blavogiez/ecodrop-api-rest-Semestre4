package utils;

import java.io.BufferedReader;
import java.io.FileReader;

import model.dao.DepositDAOPostgres;
import model.dto.Deposit;

// classe import pour importer les deposits décrits dans un fichier
public class Import {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java Import <chemin_csv>");
            System.exit(1);
        }

        DepositDAOPostgres dao = new DepositDAOPostgres();
        int importes = 0;
        int erreurs = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(args[0]))) {
            String ligne = br.readLine(); // skip header initial
            while ((ligne = br.readLine()) != null) {
                if (ligne.isBlank())
                    continue;
                try {
                    String[] champs = ligne.split(",");
                    int userId = Integer.parseInt(champs[0].trim());
                    int pointId = Integer.parseInt(champs[1].trim());
                    int wasteTypeId = Integer.parseInt(champs[2].trim());
                    double poids = Double.parseDouble(champs[3].trim());

                    Deposit d = new Deposit();
                    d.setUserId(userId);
                    d.setPointId(pointId);
                    d.setWasteTypeId(wasteTypeId);
                    d.setPoids(poids);

                    if (dao.add(d)) {
                        importes++;
                    } else {
                        System.err.println("Échec insertion ligne : " + ligne);
                        erreurs++;
                    }
                } catch (Exception e) {
                    System.err.println("Ligne invalide (" + e.getMessage() + ") : " + ligne);
                    erreurs++;
                }
            }
        }

        System.out.println("Import terminé : " + importes + " insérés, " + erreurs + " erreurs.");
    }
}
