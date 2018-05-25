package tech.lapsa.esbd.beans.dao.elements.mapping;

import static com.lapsa.kz.country.KZCity.*;

import com.lapsa.kz.country.KZCity;

public class KZCityMapping extends AMapping<Integer, KZCity> {

    private static final class SingletonHolder {
	private static final KZCityMapping HOLDER_INSTANCE = new KZCityMapping();
    }

    public static final KZCityMapping getInstance() {
	return SingletonHolder.HOLDER_INSTANCE;
    }

    private KZCityMapping() {
	addMap(ABAI, -1);
	addMap(AKKOL, -2);
	addMap(AKSAI, -3);
	addMap(AKSU, -4);
	addMap(AKTAU, 6);
	addMap(AKTOBE, 2);
	addMap(AKTOBE, 8);
	addMap(ALGA, -5);
	addMap(ALM, 13);
	addMap(ALM, 16);
	addMap(ALM, 40);
	addMap(ARAL, -6);
	addMap(ARKAL, -7);
	addMap(ARYS, -8);
	addMap(AST, 7);
	addMap(ATB, -9);
	addMap(ATY, 3);
	addMap(ATY, 4);
	addMap(AYAG, -10);
	addMap(BAIKON, -11);
	addMap(BALH, -12);
	addMap(BULA, -13);
	addMap(DERZH, -14);
	addMap(EREIM, -15);
	addMap(ESIK, -16);
	addMap(ESIL, -17);
	addMap(ZHANO, -18);
	addMap(ZHANAT, -19);
	addMap(ZHARK, -20);
	addMap(ZHZKZ, -21);
	addMap(ZHEM, -22);
	addMap(ZHETYSAY, -23);
	addMap(ZHITIKARA, -24);
	addMap(ZAISAN, -25);
	addMap(ZYRIAN, -26);
	addMap(KAZAL, -27);
	addMap(KANDYA, -28);
	addMap(KAPCH, -29);
	addMap(KGND, 9);
	addMap(KGND, 52);
	addMap(KARAZH, -30);
	addMap(KARAT, -31);
	addMap(KARKAR, -32);
	addMap(KASKEL, -33);
	addMap(KENTAU, -34);
	addMap(KOKSH, -35);
	addMap(KOSTN, 54);
	addMap(KYLSAR, -37);
	addMap(KURCH, -38);
	addMap(KYZYL, 51);
	addMap(LENGER, -40);
	addMap(LISAK, -41);
	addMap(MAKIN, -42);
	addMap(MAMLY, -43);
	addMap(PAVL, 10);
	addMap(PETRP, -44);
	addMap(PRIOZ, -45);
	addMap(RIDDR, -46);
	addMap(RUDNI, -47);
	addMap(SARAN, -48);
	addMap(SARKND, -49);
	addMap(SARYAG, -50);
	addMap(SATP, -51);
	addMap(SEMEI, 53);
	addMap(SERGEE, -53);
	addMap(SEREBR, -54);
	addMap(STEPNOG, -55);
	addMap(STEPNY, -56);
	addMap(TAIYN, -57);
	addMap(TALGAR, -58);
	addMap(TALDYK, -59);
	addMap(TARAZ, 12);
	addMap(TEKEL, -60);
	addMap(TEMIR, -61);
	addMap(TEMRT, -62);
	addMap(TURK, -63);
	addMap(URALS, -64);
	addMap(UKAM, -65);
	addMap(USHAR, -66);
	addMap(USHTB, -67);
	addMap(FRSH, -68);
	addMap(HROM, -69);
	addMap(SHARD, -70);
	addMap(SHALK, -71);
	addMap(SHAR, -72);
	addMap(SHKH, -73);
	addMap(SHMN, -74);
	addMap(SHU, -75);
	addMap(SHYM, 11);
	addMap(SCHU, -76);
	addMap(EKIB, -77);
	addMap(EMBA, -78);

	addException(1);
	addException(5);
	addException(14);
	addException(19);
	addException(20);
	addException(21);
	addException(22);
	addException(23);
	addException(26);
	addException(27);
	addException(30);
	addException(29);
	addException(31);
	addException(32);
	addException(33);
	addException(34);
	addException(35);
	addException(36);
	addException(37);
	addException(38);
	addException(39);
	addException(41);
	addException(42);
	addException(43);
	addException(44);
	addException(45);
	addException(46);
	addException(47);
	addException(48);
	addException(49);
	addException(50);
	addException(55);
	addException(56);
	addException(57);
    }
}
