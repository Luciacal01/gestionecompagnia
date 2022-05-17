package it.prova.dao.gestionecompagnia.compagnia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Statement;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class CompagniaDAOImpl extends AbstractMySQLDAO implements CompagniaDAO {

	public CompagniaDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Compagnia> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connesione non attiva. Impossibile effetuare connesioni DAO");
		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;
		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("select * from compagnia")) {
			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setId(rs.getLong("ID"));
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
				result.add(compagniaTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;

	}

	@Override
	public Compagnia get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connesione non attiva. Impossibile effetuare connesioni DAO");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Compagnia result = null;
		try (PreparedStatement ps = connection.prepareStatement("Select * from compagnia where id=?;")) {
			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Compagnia();
					result.setId(rs.getLong("ID"));
					result.setRagioneSociale(rs.getString("RAGIONE SOCIALE"));
					result.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
					result.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
				} else {
					result = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int update(Compagnia compagniaInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;

		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE compagnia SET ragionesociale=?, fatturatoannuo=?, datafondazione=? where id=?")) {
			ps.setString(1, compagniaInput.getRagioneSociale());
			ps.setInt(2, compagniaInput.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(compagniaInput.getDataFondazione().getTime()));
			ps.setLong(4, compagniaInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Compagnia compagniaInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO compagnia(ragionesociale, fatturatoannuo, datafondazione) VALUES (?,?,?);")) {
			ps.setString(1, compagniaInput.getRagioneSociale());
			ps.setInt(2, compagniaInput.getFatturatoAnnuo());
			ps.setDate(3, new java.sql.Date(compagniaInput.getDataFondazione().getTime()));
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Compagnia compagniaInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null || compagniaInput.getId() == null || compagniaInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		List<Impiegato> listaImpiegati = compagniaInput.getImpiegati();
		if (listaImpiegati.size() > 0)
			throw new Exception("impossibile cancellare la compagnia sono ancora presnti impiegati");

		int result = 0;

		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM COMPAGNIA WHERE ID=?")) {
			ps.setLong(1, compagniaInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Compagnia> findByExample(Compagnia input) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (input == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Compagnia> result = new ArrayList<Compagnia>();
		Compagnia compagniaTemp = null;

		String query = " SELECT * FROM compagnia WHERE 1=1 ";
		if (input.getRagioneSociale() != null && !input.getRagioneSociale().isEmpty()) {
			query += " and ragionesociale like '" + input.getRagioneSociale() + "%' ";
		}
		if (input.getFatturatoAnnuo() > 0) {
			query += " and fatturatoannuo like '" + input.getFatturatoAnnuo() + "%' ";
		}
		if (input.getDataFondazione() != null) {
			query += " and datafondazione='" + new java.sql.Date(input.getDataFondazione().getTime());
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				compagniaTemp = new Compagnia();
				compagniaTemp.setRagioneSociale(rs.getString("RAGIONESOCIALE"));
				compagniaTemp.setFatturatoAnnuo(rs.getInt("FATTURATOANNUO"));
				compagniaTemp.setDataFondazione(rs.getDate("DATAFONDAZIONE"));
				compagniaTemp.setId(rs.getLong("ID"));
				result.add(compagniaTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	public void findByIdEager(Compagnia compagniaInput) throws Exception {

		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (compagniaInput == null)
			throw new Exception("Valore di input non ammesso.");
		List<Impiegato> elencoImpiegatiPresenti = new ArrayList<Impiegato>();
		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement(
				"select i.id, i.nome, i.cognome, i.codicefiscale, i.datanascita, i.dataassunzione from compagnia c left outer join impiegato i on c.id = i.compagnia_id where c.id=?")) {

			ps.setLong(1, compagniaInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = new Impiegato();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));

					elencoImpiegatiPresenti.add(result);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		compagniaInput.setImpiegati(elencoImpiegatiPresenti);
	}

	@Override
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataAssunzioneInput) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Compagnia> findAllByRagioneSocialeContiene(String stringaDaVerificare) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Compagnia> findAllByCodiceFiscaleImpiegatoContiene(String stringaDaVerificare) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
