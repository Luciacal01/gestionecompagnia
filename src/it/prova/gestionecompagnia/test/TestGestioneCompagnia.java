package it.prova.gestionecompagnia.test;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.management.RuntimeErrorException;

import it.prova.gestionecompagnia.connection.MyConnection;
import it.prova.gestionecompagnia.dao.Constants;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAO;
import it.prova.gestionecompagnia.dao.compagnia.CompagniaDAOImpl;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAO;
import it.prova.gestionecompagnia.dao.impiegato.ImpiegatoDAOImpl;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class TestGestioneCompagnia {
	public static void main(String[] args) {
		CompagniaDAO companiaDAOInstance = null;
		ImpiegatoDAO impiegatoDAOInstance = null;

		try (Connection connection = MyConnection.getConnection(Constants.DRIVER_NAME, Constants.CONNECTION_URL)) {
			companiaDAOInstance = new CompagniaDAOImpl(connection);
			impiegatoDAOInstance = new ImpiegatoDAOImpl(connection);

			System.out.println("in tabella compagnia ci sono: " + companiaDAOInstance.list().size() + " elementi.");
			System.out.println("in tabella impiegato ci sono: " + impiegatoDAOInstance.list().size() + " elementi.");

			// testInsertCompagnia(companiaDAOInstance);
			// System.out.println("in tabella compagnia ci sono: " +
			// companiaDAOInstance.list().size() + " elementi.");

			// testInsertImpiegato(impiegatoDAOInstance, companiaDAOInstance);
			// System.out.println("in tabella impiegati ci sono: " +
			// impiegatoDAOInstance.list().size() + " elementi.");

			// testUpdateCompagnia(companiaDAOInstance);
			// testUpdateImpiegato(impiegatoDAOInstance);
			// testDeleteCompagnia(companiaDAOInstance, impiegatoDAOInstance);
			testFindByExampleCompagnia(companiaDAOInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void testInsertCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("..........inizio testInsertCompagnia........");
		int quantiElementiInseriti = compagniaDAOInstance.insert(new Compagnia("celiop", 59000, new Date()));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertCompagnia: FAILED");
		System.out.println("..........inizio testInsertCompagnia: PASSED.......");
	}

	public static void testInsertImpiegato(ImpiegatoDAO impiegatoDAOInstance, CompagniaDAO compagniaDAOInstance)
			throws Exception {
		System.out.println("..........inizio testInsertImpiegato........");
		List<Compagnia> listaCompagnie = compagniaDAOInstance.list();
		int quantiElementiInseriti = impiegatoDAOInstance.insert(new Impiegato(null, "Giulia", "Ciztro",
				"GLUCZR59G689KASDKJ", new Date(), new Date(), listaCompagnie.get(3)));
		if (quantiElementiInseriti < 1)
			throw new RuntimeException("testInsertElementi: FAILED");
		System.out.println("..........inizio testInsertElementi: PASSED.......");

	}

	public static void testUpdateCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println("..........inizio testUpdateCompagnia........");
		Compagnia compDaSostituire = new Compagnia(2L, "Simar", 950000, new Date());
		int verificaElementiModificati = compagniaDAOInstance.update(compDaSostituire);

		if (verificaElementiModificati < 0)
			throw new RuntimeException("testUpdateCompagnia: FAILED");

		System.out.println(".......testUpdateCompagnia: PASSED........");
	}

	public static void testUpdateImpiegato(ImpiegatoDAO impiegatoDAOInstance) throws Exception {
		System.out.println("..........inizio testUpdateImpiegato........");
		Date dataNascita = new SimpleDateFormat("dd-MM-yyyy").parse("27-07-1998");
		Date dataAssunzione = new SimpleDateFormat("dd-MM-yyyy").parse("11-11-2018");
		Impiegato impDaSostituire = new Impiegato(5L, "Valerio", "Atto", "VLRTAO04J410SBHAXKJ", dataNascita,
				dataAssunzione);
		int verificaElementiModificati = impiegatoDAOInstance.update(impDaSostituire);

		if (verificaElementiModificati < 0)
			throw new RuntimeException("testUpdateImpiegato: FAILED");

		System.out.println(".......testUpdateImpiegato: PASSED........");
	}

	public static void testDeleteCompagnia(CompagniaDAO compagniaDAOInstance, ImpiegatoDAO impiegatoDAOInstance)
			throws Exception {
		System.out.println(".......testDeleteImpiegato inizio.......");

		int compagniaCancellata = 0;
		List<Compagnia> listaCompagnie = compagniaDAOInstance.list();
		if (listaCompagnie.size() < 1)
			throw new Exception("testDeleteCompagnia: FAILED, non ci sono compagnie");

		Compagnia compagniaDaRimuovere = listaCompagnie.get(3);
		compagniaDAOInstance.findByIdEager(compagniaDaRimuovere);

		if (compagniaDaRimuovere.getImpiegati().size() > 0) {
			for (Impiegato impiegatoItem : compagniaDaRimuovere.getImpiegati()) {
				impiegatoDAOInstance.delete(impiegatoItem);
				compagniaDaRimuovere.getImpiegati().remove(impiegatoItem);
			}
			compagniaCancellata = compagniaDAOInstance.delete(compagniaDaRimuovere);
		} else {
			compagniaCancellata = compagniaDAOInstance.delete(compagniaDaRimuovere);
		}

		if (compagniaCancellata < 1)
			throw new RuntimeException("testDeleteCompagnia: FAILED, l'appartamento non è stato cancellato");
		System.out.println("..........testDeleteCompagnia: PASSED.......");
	}

	public static void testFindByExampleCompagnia(CompagniaDAO compagniaDAOInstance) throws Exception {
		System.out.println(".......testFindByExample.........");
		Date dataFondazione = new SimpleDateFormat("dd-MM-yyyy").parse("17-05-2022");
		Compagnia compagniaDaRicercare = new Compagnia("simar", 0, null);
		List<Compagnia> compagnie = compagniaDAOInstance.findByExample(compagniaDaRicercare);
		if (compagnie.size() < 0)
			throw new RuntimeException("Test : FAILED");
		for (Compagnia compagniaItem : compagnie) {
			System.out.println(compagniaItem.getRagioneSociale() + " " + compagniaItem.getFatturatoAnnuo() + " "
					+ compagniaItem.getDataFondazione());

		}
	}

}
