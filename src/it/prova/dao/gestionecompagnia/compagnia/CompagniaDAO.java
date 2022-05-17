package it.prova.dao.gestionecompagnia.compagnia;

import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;

public interface CompagniaDAO extends IBaseDAO<Compagnia> {
	public List<Compagnia> findAllByDataAssunzioneMaggioreDi(Date dataAssunzioneInput) throws Exception;

	public List<Compagnia> findAllByRagioneSocialeContiene(String stringaDaVerificare) throws Exception;

	public List<Compagnia> findAllByCodiceFiscaleImpiegatoContiene(String stringaDaVerificare) throws Exception;
}
