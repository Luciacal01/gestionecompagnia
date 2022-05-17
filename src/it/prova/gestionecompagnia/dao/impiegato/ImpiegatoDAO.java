package it.prova.gestionecompagnia.dao.impiegato;

import java.util.Date;
import java.util.List;

import it.prova.gestionecompagnia.dao.AbstractMySQLDAO;
import it.prova.gestionecompagnia.dao.IBaseDAO;
import it.prova.gestionecompagnia.model.Compagnia;
import it.prova.gestionecompagnia.model.Impiegato;

public interface ImpiegatoDAO extends IBaseDAO<Impiegato> {
	public List<Impiegato> findAllByCompagnia(Compagnia compagniaInput) throws Exception;

	public int countByDataFondazioneCompagniaRatherThan(Date dataFondazioeDaConfrontare) throws Exception;

	public List<Impiegato> findAllByCompagniaConFatturatoMaggioreDi(int fatturatoDaConfrontare) throws Exception;

	public List<Impiegato> findAllErrorAssunzione() throws Exception;
}
