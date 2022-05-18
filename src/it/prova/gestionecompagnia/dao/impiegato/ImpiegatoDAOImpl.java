package it.prova.gestionecompagnia.dao.impiegato;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public class ImpiegatoDAOImpl extends AbstractMySQLDAO implements ImpiegatoDAO {

	public ImpiegatoDAOImpl(Connection connection) {
		super(connection);
	}

	@Override
	public List<Impiegato> list() throws Exception {
		if (isNotActive())
			throw new Exception("Connesione non attiva. Impossibile effetuare connesioni DAO");
		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		try (Statement ps = connection.createStatement(); ResultSet rs = ps.executeQuery("Select * from impiegato;")) {
			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setId(rs.getLong("ID"));
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
				impiegatoTemp.setDataAssunzione(rs.getDate("DATANASCITA"));

				result.add(impiegatoTemp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public Impiegato get(Long idInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connesione non attiva. Impossibile effetuare connesioni DAO");

		if (idInput == null || idInput < 1)
			throw new Exception("Valore di input non ammesso.");

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement("Select * from impiegato where id=?;")) {
			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Impiegato();
					result.setId(rs.getLong("ID"));
					result.setNome(rs.getString("NOME"));
					result.setCognome(rs.getString("COGNOME"));
					result.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					result.setDataNascita(rs.getDate("DATANASCITA"));
					result.setDataAssunzione(rs.getDate("DATANASCITA"));

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
	public int update(Impiegato impiegatoInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null || impiegatoInput.getId() == null || impiegatoInput.getId() < 1)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;

		try (PreparedStatement ps = connection.prepareStatement(
				"UPDATE impiegato SET nome=?, cognome=?, codicefiscale=?, datanascita=?, dataassunzione=? where id=?;")) {
			ps.setString(1, impiegatoInput.getNome());
			ps.setString(2, impiegatoInput.getCognome());
			ps.setString(3, impiegatoInput.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(impiegatoInput.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(impiegatoInput.getDataAssunzione().getTime()));
			ps.setLong(6, impiegatoInput.getId());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int insert(Impiegato impiegatoInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				"INSERT INTO impiegato(nome, cognome, codicefiscale, datanascita, dataassunzione, compagnia_id) VALUES (?,?,?,?,?,?);")) {
			ps.setString(1, impiegatoInput.getNome());
			ps.setString(2, impiegatoInput.getCognome());
			ps.setString(3, impiegatoInput.getCodiceFiscale());
			ps.setDate(4, new java.sql.Date(impiegatoInput.getDataNascita().getTime()));
			ps.setDate(5, new java.sql.Date(impiegatoInput.getDataAssunzione().getTime()));
			ps.setLong(6, impiegatoInput.getCompagnia().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public int delete(Impiegato impiegatoInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null)
			throw new Exception("Valore di input non ammesso.");

		int result = 0;

		try (PreparedStatement ps = connection.prepareStatement("DELETE FROM impiegato WHERE ID=?")) {
			ps.setLong(1, impiegatoInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return result;
	}

	@Override
	public List<Impiegato> findByExample(Impiegato impiegatoInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		if (impiegatoInput == null)
			throw new Exception("Valore di input non ammesso.");

		ArrayList<Impiegato> result = new ArrayList<Impiegato>();
		Impiegato impiegatoTemp = null;

		String query = " SELECT * FROM impiegato WHERE 1=1 ";
		if (impiegatoInput.getNome() != null && !impiegatoInput.getNome().isEmpty()) {
			query += " and nome like '" + impiegatoInput.getNome() + "%' ";
		}
		if (impiegatoInput.getCognome() != null && !impiegatoInput.getCognome().isEmpty()) {
			query += " and cognome like '" + impiegatoInput.getCognome() + "%' ";
		}

		if (impiegatoInput.getCodiceFiscale() != null && !impiegatoInput.getCodiceFiscale().isEmpty()) {
			query += " and codicefiscale like '" + impiegatoInput.getCodiceFiscale() + "%' ";
		}

		if (impiegatoInput.getDataNascita() != null) {
			query += " and datanascita='" + new java.sql.Date(impiegatoInput.getDataNascita().getTime());
		}

		if (impiegatoInput.getDataAssunzione() != null) {
			query += " and dataassunzione='" + new java.sql.Date(impiegatoInput.getDataAssunzione().getTime());
		}

		if (impiegatoInput.getCompagnia().getId() > 0) {
			query += "and compagnia_id='" + impiegatoInput.getCompagnia().getId() + "%' ";
		}

		try (Statement ps = connection.createStatement()) {
			ResultSet rs = ps.executeQuery(query);

			while (rs.next()) {
				impiegatoTemp = new Impiegato();
				impiegatoTemp.setId(rs.getLong("ID"));
				impiegatoTemp.setNome(rs.getString("NOME"));
				impiegatoTemp.setCognome(rs.getString("COGNOME"));
				impiegatoTemp.setCodiceFiscale(rs.getString("CODICEFISCALE"));
				impiegatoTemp.setDataNascita(rs.getDate("DATANASCITA"));
				impiegatoTemp.setDataAssunzione(rs.getDate("DATANASCITA"));
				// impiegatoTemp.setCompagnia(rs.getLong("COMPAGNIA_ID"));
				result.add(impiegatoTemp);
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

		Impiegato result = null;
		try (PreparedStatement ps = connection.prepareStatement(
				"select * from compagnia c left outer join impiegato i on c.id = i.compagnia_id where id=?")) {

			ps.setLong(1, compagniaInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = new Impiegato();
					result.setNome(rs.getString("nome"));
					result.setCognome(rs.getString("cognome"));
					result.setCodiceFiscale(rs.getString("codicefiscale"));
					result.setDataNascita(rs.getDate("datanascita"));
					result.setDataAssunzione(rs.getDate("dataassunzione"));

					compagniaInput.getImpiegati().add(result);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		List<Impiegato> impiegati = new ArrayList<Impiegato>();
		Impiegato impiegato = null;

		try (PreparedStatement ps = connection.prepareStatement("Select * from impiegato where compagnia_id=?;")) {
			ps.setLong(1, compagniaInput.getId());
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					impiegato = new Impiegato();
					impiegato.setNome(rs.getString("NOME"));
					impiegato.setCognome(rs.getString("COGNOME"));
					impiegato.setCodiceFiscale(rs.getString("CODICEFISCALE"));
					impiegato.setDataNascita(rs.getDate("DATANASCITA"));
					impiegato.setDataAssunzione(rs.getDate("DATAASSUNZIONE"));
					impiegato.setId(rs.getLong("ID"));
					impiegati.add(impiegato);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return impiegati;
	}

	@Override
	public int countByDataFondazioneCompagniaRatherThan(Date dataFondazioeDaConfrontare) throws Exception {
		if (isNotActive())
			throw new Exception("Connessione non attiva. Impossibile effettuare operazioni DAO.");

		int contatoreMaggioreDI = 0;
		try (PreparedStatement ps = connection.prepareStatement(
				" select count(*) from impiegato i inner join compagnia c on i.compagnia_id=c.id where c.datafondazione>?;")) {
			ps.setDate(1, new java.sql.Date(dataFondazioeDaConfrontare.getTime()));
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					contatoreMaggioreDI = rs.getInt("count(*)");
				}
			}
		}

		return contatoreMaggioreDI;
	}

	@Override
	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoDaConfrontare) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Impiegato> findAllErrorAssunzione() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
