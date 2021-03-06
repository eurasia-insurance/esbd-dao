package tech.lapsa.esbd.beans.dao.elements.mapping;

import com.lapsa.international.country.Country;

public final class CountryMapping extends ElementsMapping<Integer, Country> {

    private static final class SingletonHolder {
        private static final CountryMapping HOLDER_INSTANCE = new CountryMapping();
    }

    public static final CountryMapping getInstance() {
        return SingletonHolder.HOLDER_INSTANCE;
    }

    private CountryMapping() {
        addMap(Country.ABW, 29);
        addMap(Country.AFG, 30);
        addMap(Country.AGO, 23);
        addMap(Country.AIA, 22);
        addMap(Country.ALB, 20);
        addMap(Country.AND, 24);
        addMap(Country.ANT, 26);
        addMap(Country.ARE, 141);
        addMap(Country.ARG, 27);
        addMap(Country.ARM, 28);
        addMap(Country.ASM, 75);
        addMap(Country.ATG, 25);
        addMap(Country.AUS, 17);
        addMap(Country.AUT, 18);
        addMap(Country.AZE, 19);
        addMap(Country.BDI, 46);
        addMap(Country.BEL, 13);
        addMap(Country.BEN, 37);
        addMap(Country.BFA, 45);
        addMap(Country.BGD, 32);
        addMap(Country.BGR, 42);
        addMap(Country.BHR, 34);
        addMap(Country.BHS, 31);
        addMap(Country.BIH, 40);
        addMap(Country.BLM, -652);
        addMap(Country.BLR, 35);
        addMap(Country.BLZ, 36);
        addMap(Country.BMU, 38);
        addMap(Country.BOL, 43);
        addMap(Country.BRA, 44);
        addMap(Country.BRB, 33);
        addMap(Country.BRN, -96);
        addMap(Country.BTN, 47);
        addMap(Country.BWA, 41);
        addMap(Country.CAF, 194);
        addMap(Country.CAN, 90);
        addMap(Country.CCK, -166);
        addMap(Country.CHE, 8);
        addMap(Country.CHL, 197);
        addMap(Country.CHN, 95);
        addMap(Country.CIV, 102);
        addMap(Country.CMR, 89);
        addMap(Country.COD, -180);
        addMap(Country.COG, 99);
        addMap(Country.COK, 105);
        addMap(Country.COL, 97);
        addMap(Country.COM, 98);
        addMap(Country.CPV, 86);
        addMap(Country.CRI, 101);
        addMap(Country.CUB, 103);
        addMap(Country.CXR, -162);
        addMap(Country.CYM, 87);
        addMap(Country.CYP, 93);
        addMap(Country.CZE, 196);
        addMap(Country.DEU, 5);
        addMap(Country.DEU, 208);
        addMap(Country.DJI, 71);
        addMap(Country.DMA, 72);
        addMap(Country.DNK, 70);
        addMap(Country.DOM, 73);
        addMap(Country.DZA, 21);
        addMap(Country.ECU, 201);
        addMap(Country.EGY, 10);
        addMap(Country.ERI, 203);
        addMap(Country.ESP, 84);
        addMap(Country.EST, 200);
        addMap(Country.ETH, 204);
        addMap(Country.FIN, 192);
        addMap(Country.FJI, 190);
        addMap(Country.FLK, -238);
        addMap(Country.FRA, 12);
        addMap(Country.FRO, 189);
        addMap(Country.FSM, 124);
        addMap(Country.GAB, 55);
        addMap(Country.GBR, 7);
        addMap(Country.GBR, 209);
        addMap(Country.GEO, 68);
        addMap(Country.GGY, -831);
        addMap(Country.GHA, 59);
        addMap(Country.GIB, 63);
        addMap(Country.GIN, 61);
        addMap(Country.GLP, 217, -312);
        addMap(Country.GMB, 58);
        addMap(Country.GNB, 62);
        addMap(Country.GNQ, 202);
        addMap(Country.GRC, 67);
        addMap(Country.GRD, 65);
        addMap(Country.GRL, 66);
        addMap(Country.GTM, 60);
        addMap(Country.GUF, -254);
        addMap(Country.GUM, 69);
        addMap(Country.GUY, 57);
        addMap(Country.HKG, 218, -344);
        addMap(Country.HND, 64);
        addMap(Country.HRV, 193);
        addMap(Country.HTI, 56);
        addMap(Country.HUN, 49);
        addMap(Country.IDN, 79);
        addMap(Country.IMN, 4);
        addMap(Country.IND, 78);
        addMap(Country.IOT, -86);
        addMap(Country.IRL, 11);
        addMap(Country.IRN, 82);
        addMap(Country.IRQ, 81);
        addMap(Country.ISL, 83);
        addMap(Country.ISR, 77);
        addMap(Country.ITA, 14);
        addMap(Country.JAM, 207);
        addMap(Country.JEY, -832);
        addMap(Country.JOR, 80);
        addMap(Country.JPN, 16);
        addMap(Country.KAZ, 1);
        addMap(Country.KEN, 92);
        addMap(Country.KGZ, 9);
        addMap(Country.KHM, 88);
        addMap(Country.KIR, 94);
        addMap(Country.KNA, 164);
        addMap(Country.KOR, 96);
        addMap(Country.KWT, 104);
        addMap(Country.LAO, 106);
        addMap(Country.LBN, 110);
        addMap(Country.LBR, 111);
        addMap(Country.LBY, 109);
        addMap(Country.LCA, 165);
        addMap(Country.LIE, 113);
        addMap(Country.LKA, 199);
        addMap(Country.LSO, 108);
        addMap(Country.LTU, 112);
        addMap(Country.LUX, 213);
        addMap(Country.LVA, 107);
        addMap(Country.MAC, 222, -446);
        addMap(Country.MAF, 214);
        addMap(Country.MAR, 130);
        addMap(Country.MCO, 127);
        addMap(Country.MDA, 126);
        addMap(Country.MDG, 115);
        addMap(Country.MDV, 215, -462);
        addMap(Country.MEX, 123);
        addMap(Country.MHL, 122);
        addMap(Country.MKD, 116);
        addMap(Country.MLI, 120);
        addMap(Country.MLT, 119);
        addMap(Country.MMR, 39);
        addMap(Country.MMR, 131);
        addMap(Country.MNE, 212);
        addMap(Country.MNG, 128);
        addMap(Country.MNP, 121);
        addMap(Country.MOZ, 125);
        addMap(Country.MRT, 220, -478);
        addMap(Country.MSR, 129);
        addMap(Country.MUS, 114);
        addMap(Country.MWI, 117);
        addMap(Country.MYS, 118);
        addMap(Country.MYT, 221, -175);
        addMap(Country.NAM, 132);
        addMap(Country.NCL, -540);
        addMap(Country.NER, 135);
        addMap(Country.NFK, -574);
        addMap(Country.NGA, 136);
        addMap(Country.NIC, 140);
        addMap(Country.NIU, -570);
        addMap(Country.NLD, 139);
        addMap(Country.NOR, 138);
        addMap(Country.NPL, 134);
        addMap(Country.NRU, 133);
        addMap(Country.NZL, 137);
        addMap(Country.OMN, 142);
        addMap(Country.PAK, 143);
        addMap(Country.PAN, 145);
        addMap(Country.PAN, 146);
        addMap(Country.PCN, -612);
        addMap(Country.PER, 150);
        addMap(Country.PHL, 191);
        addMap(Country.PLW, 144);
        addMap(Country.PNG, 147);
        addMap(Country.PNG, 148);
        addMap(Country.POL, 152);
        addMap(Country.PRI, 225, -630);
        addMap(Country.PRK, 100);
        addMap(Country.PRT, 153);
        addMap(Country.PRY, 149);
        addMap(Country.PSE, 211);
        addMap(Country.PYF, 151);
        addMap(Country.QAT, 91);
        addMap(Country.REU, -638);
        addMap(Country.ROU, 155);
        addMap(Country.RUS, 2);
        addMap(Country.RWA, 154);
        addMap(Country.SAU, 159);
        addMap(Country.SDN, 172);
        addMap(Country.SEN, 163);
        addMap(Country.SGP, 166);
        addMap(Country.SGS, -239);
        addMap(Country.SHN, -654);
        addMap(Country.SJM, -744);
        addMap(Country.SLB, 170);
        addMap(Country.SLE, 174);
        addMap(Country.SLV, 156);
        addMap(Country.SMR, 157);
        addMap(Country.SOM, 171);
        addMap(Country.SPM, 161);
        addMap(Country.SRB, 210);
        addMap(Country.STP, 158);
        addMap(Country.SUR, 173);
        addMap(Country.SVK, 168);
        addMap(Country.SVN, 169);
        addMap(Country.SWE, 198);
        addMap(Country.SWZ, 160);
        addMap(Country.SYC, 162);
        addMap(Country.SYR, 167);
        addMap(Country.TCA, -796);
        addMap(Country.TCD, 195);
        addMap(Country.TGO, 181);
        addMap(Country.THA, 177);
        addMap(Country.TJK, 175);
        addMap(Country.TKM, 183);
        addMap(Country.TLS, -626);
        addMap(Country.TON, 180);
        addMap(Country.TTO, 179);
        addMap(Country.TUN, 15);
        addMap(Country.TUR, 3);
        addMap(Country.TUV, 182);
        addMap(Country.TWN, 176);
        addMap(Country.TZA, 178);
        addMap(Country.UGA, 184);
        addMap(Country.UKR, 185);
        addMap(Country.URY, 188);
        addMap(Country.USA, 6);
        addMap(Country.UZB, 186);
        addMap(Country.VAT, 48);
        addMap(Country.VCT, -670);
        addMap(Country.VEN, 50);
        addMap(Country.VGB, 51);
        addMap(Country.VIR, 52);
        addMap(Country.VNM, 54);
        addMap(Country.VUT, 216, -548);
        addMap(Country.WLF, 187);
        addMap(Country.WSM, 53);
        addMap(Country.YEM, 85);
        addMap(Country.YUG, 205);
        addMap(Country.ZAF, 206);
        addMap(Country.ZMB, 74);
        addMap(Country.ZWE, 76);
        addMap(Country.ESH, 219);
        addMap(Country.MTQ, 223);
        addException(224);
    }
}
