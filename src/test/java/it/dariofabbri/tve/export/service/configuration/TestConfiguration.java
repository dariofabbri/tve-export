package it.dariofabbri.tve.export.service.configuration;

import junit.framework.Assert;
import it.dariofabbri.tve.export.model.DatiFiscali;
import it.dariofabbri.tve.export.model.Fornitore;
import it.dariofabbri.tve.export.service.configuration.Configuration;

import org.junit.Test;

public class TestConfiguration {

	@Test
	public void test1() {
		
		Configuration configuration = Configuration.getInstance();
		configuration.reset();
		
		Fornitore fornitore = configuration.getFornitore();
		fornitore.setDescrizione("TVE VIGILANZA L. S.r.l.");
		fornitore.setDescrizione2("ISTITUTO di VIGILANZA");
		fornitore.setIndirizzo("Via del Podere Fiume n. 4");
		fornitore.setCitta("ROMA");
		fornitore.setProvincia("RM");
		fornitore.setCap("00168");
		fornitore.setCodicePaese("ITA");
		
		DatiFiscali datiFiscaliFornitore = fornitore.getDatiFiscali();
		datiFiscaliFornitore.setPartitaIva("07147091008");
		datiFiscaliFornitore.setCapitaleSociale("10000€");
		datiFiscaliFornitore.setNumeroRegistroImprese("1013892");
		fornitore.setDatiFiscali(datiFiscaliFornitore);

		configuration.save();
	}

	
	@Test
	public void test2() {
		
		Configuration configuration = Configuration.getInstance();
		configuration.reset();
		configuration.load();
		
		Assert.assertEquals("TVE VIGILANZA L. S.r.l.", configuration.getFornitore().getDescrizione());
		Assert.assertEquals("ISTITUTO di VIGILANZA", configuration.getFornitore().getDescrizione2());
		Assert.assertEquals("Via del Podere Fiume n. 4", configuration.getFornitore().getIndirizzo());
		Assert.assertEquals("ROMA", configuration.getFornitore().getCitta());
		Assert.assertEquals("RM", configuration.getFornitore().getProvincia());
		Assert.assertEquals("00168", configuration.getFornitore().getCap());
		Assert.assertEquals("ITA", configuration.getFornitore().getCodicePaese());
		Assert.assertEquals("07147091008", configuration.getFornitore().getDatiFiscali().getPartitaIva());
		Assert.assertEquals("10000€", configuration.getFornitore().getDatiFiscali().getCapitaleSociale());
		Assert.assertEquals("1013892", configuration.getFornitore().getDatiFiscali().getNumeroRegistroImprese());
	}

	
	@Test
	public void test3() {
		
		Configuration configuration = Configuration.getInstance();
		Assert.assertFalse(configuration.isValid());
		configuration.load();
		Assert.assertTrue(configuration.isValid());
	}
}
