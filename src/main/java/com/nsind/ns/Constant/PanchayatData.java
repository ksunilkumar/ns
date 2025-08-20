package com.nsind.ns.Constant;
import java.util.*;

public class PanchayatData {
        // Map<BlockName, Map<PanchayatName, PanchayatCode>>
        static public final Map<String, Map<String, String>> DATA = new LinkedHashMap<>();
        
        static public final Map<String, String> blockMap = new HashMap<>();

        static public String norm(String s) { return s == null ? "" : s.trim().toUpperCase(); }

        static public String findCodeByName(String name) {
            if (name == null) return null;
            String needle = name.trim();
            for (Map<String, String> block : DATA.values()) {
                // exact
                if (block.containsKey(needle)) return block.get(needle);
                // case-insensitive fallback
                for (Map.Entry<String, String> e : block.entrySet()) {
                    if (e.getKey().equalsIgnoreCase(needle)) return e.getValue();
                }
            }
            return null;
        }
        

        static {
            // Register each block map
            DATA.put(norm("BUNGTE CHIRU TD BLOCK"), BungteChiru.block());
            DATA.put(norm("CHAMPAI TD BLOCK"), Champai.block());
            DATA.put(norm("ISLAND TD BLOCK"), Island.block());
            DATA.put(norm("KANGCHUP GELJANG TD BLOCK"), KangchupGeljang.block());
            DATA.put(norm("KANGPOKPI"), Kangpokpi.block());
            DATA.put(norm("LHUNGTIN TD BLOCK"), Lhungtin.block());
            DATA.put(norm("SAIKUL"), Saikul.block());
            DATA.put(norm("SAITU GAMPHAZOL"), SaituGamphazol.block());
            DATA.put(norm("T. VAICHONG TD BLOCK"), TVaichong.block());
            // If you add more blocks later, register them here in the same style.
            
            blockMap.put("BUNGTE CHIRU TD BLOCK", "2001015");
            blockMap.put("CHAMPAI TD BLOCK", "2001010");
            blockMap.put("ISLAND TD BLOCK", "2001012");
            blockMap.put("KANGCHUP GELJANG TD BLOCK", "2001013");
            blockMap.put("KANGPOKPI", "2001003");
            blockMap.put("LHUNGTIN TD BLOCK", "2001014");
            blockMap.put("SAIKUL", "2001004");
            blockMap.put("SAITU GAMPHAZOL", "2001007");
            blockMap.put("T. VAICHONG TD BLOCK", "2001011");
        }
        


        // ====== Block: BUNGTE CHIRU TD BLOCK ======
        static final class BungteChiru {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                
                // Expanded codes you later provided:
                m.put("Bungte Chiru", "2001007108");
                m.put("Bungte khullen", "2001007192");
                m.put("Chini Ingkhol", "2001007002");
                m.put("Ichum Keirap", "2001007043");
                m.put("Joupi", "2001007195");
                m.put("Khoirok", "2001007136");
                m.put("Khoripok", "2001007056");
                m.put("Laimaton Thangbuh", "2001007063");
                m.put("Loibol Khullen", "2001007015");
                m.put("Loibol Khunou", "2001007115");
                m.put("Manamjang", "2001007158");
                m.put("Mongbung Tongneh", "2001007069");
                m.put("Nunggang(Chongphun)", "2001007165");
                m.put("Nungsai Chiru", "2001007019");
                m.put("Parengba", "2001007169");
                m.put("Sadar Joute", "2001007173");
                m.put("Sadu chiru", "2001007174");
                m.put("Saingai Namdai", "2001007175");
                m.put("Tingkai Khullen", "2001007025");
                m.put("Tingkai Khunou", "2001007101");
                m.put("Tuikhang Aimol", "2001007186");
                m.put("Wainem", "2001007187");
                m.put("Wajang Mangkhosom", "2001007188");
                m.put("Waroiching", "2001007190");
                return m;
            }
        }

        // ====== Block: CHAMPAI TD BLOCK ======
        static final class Champai {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();

                // Expanded list:
                m.put("Bolsang", "2001003005");
                m.put("Bongmoul", "2001003006");
                m.put("C.Lamjang", "2001003007");
                m.put("Canaanphai", "2001003008");
                m.put("Chandmari", "2001003010");
                m.put("Gamnomphai", "2001003020");
                m.put("Gopibung", "2001003025");
                m.put("Haipi", "2001003135");
                m.put("J Munnom", "2001003037");
                m.put("Janglenphai", "2001003038");
                m.put("Jangmol", "2001003039");
                m.put("Jangnom", "2001003040");
                m.put("K Gamnom", "2001003041");
                m.put("K. Sajal", "2001003042");
                m.put("K.Moljol", "2001003043");
                m.put("K.Songtun", "2001003044");
                m.put("Keithelmanbi", "2001003151");
                m.put("Kholjang", "2001003048");
                m.put("Khonom", "2001003050");
                m.put("laloi", "2001003055");
                m.put("Leisanphung", "2001003058");
                m.put("Lhangnomphai", "2001003060");
                m.put("Molsang", "2001003068");
                m.put("N.Gamhoi", "2001003069");
                m.put("N.Phungchoijang", "2001003070");
                m.put("Natheljang", "2001003073");
                m.put("P Moljang", "2001003074");
                m.put("P.Khothah", "2001003075");
                m.put("Phailen", "2001003077");
                m.put("Phaimol", "2001003078");
                m.put("Phoibung", "2001003079");
                m.put("S Jalenmoul", "2001003083");
                m.put("S Khomunnom", "2001003084");
                m.put("S Molnom", "2001003085");
                m.put("S.M.Gilgal", "2001003087");
                m.put("Saikotjang", "2001003088");
                m.put("Saipimol Holjang", "2001003089");
                m.put("Santalbari", "2007006015");
                m.put("Santolbari", "2001003156");
                m.put("T.Lhanghoi", "2001003099");
                m.put("Thanamba", "2001003102");
                m.put("Tokpa Naga", "2001003131");
                m.put("Tujang Part-I", "2001003106");
                m.put("Tujang Part-II", "2001003107");
                m.put("Washangphung", "2001003110");
                m.put("West Selsi", "2001003113");
                m.put("West Tuipajang", "2001003114");
                return m;
            }
        }

        // ====== Block: ISLAND TD BLOCK ======
        static final class Island {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();

                // Expanded (partial shown; continue adding the rest in same pattern):
                m.put("B.Phainom", "2001004026");
                m.put("Bethelphai", "2001004027");
                m.put("Bewlahland", "2001004028");
                m.put("Bongbal Khullen", "2001004030");
                m.put("C.Moljol", "2001004032");
                m.put("Chaningpokpi", "2001004037");
                m.put("Gotangkot", "2001004053");
                m.put("Haokhongching", "2001004064");
                m.put("Happy Land", "2001004066");
                m.put("Heirokland", "2001004068");
                m.put("Irong Ngoupikhong", "2001004072");
                m.put("Irong Tangkhul", "2001004003");
                m.put("Island Tangkhul", "2001004073");
                m.put("K.Heichanglok", "2001004075");
                m.put("K.Hengjang", "2001004076");
                m.put("K.Nungmanbi", "2001004078");
                m.put("Kamu Koireng", "2001004079");
                m.put("Kamu Leirok", "2001004080");
                m.put("Kamu Maring", "2001004081");
                m.put("Kamu Tampak", "2001004082");
                m.put("Kamu Tangnom", "2001004083");
                m.put("Kamu Themdoi", "2001004084");
                m.put("Kamuching", "2001004085");
                m.put("Kamusaichang", "2001004086");
                m.put("Kaprang", "2001004087");
                m.put("Karpur Sungba", "2001004088");
                m.put("Keihao Tangkhul", "2001004089");
                m.put("Keirouchingdong", "2001004090");
                m.put("Kharan Tangkhul", "2001004094");
                m.put("Komlaching", "2001004098");
                m.put("Kwarok Maring", "2001004099");
                m.put("L.Molnom", "2001004105");
                m.put("Laikoiching", "2001004006");
                m.put("Laipharok Maring", "2001004107");
                m.put("Lairok Vaiphei", "2001004109");
                m.put("Leingangpokpi", "2001004110");
                m.put("Leishiphung", "2001004111");
                m.put("Lemba Khul", "2001004112");
                m.put("Lishamlok Tkl", "2001004117");
                m.put("Maphou Kuki", "2001004128");
                m.put("Moirangpal", "2001004129");
                m.put("Molkonbung", "2001004134");
                m.put("Monglham", "2001004138");
                m.put("N.Heikon", "2001004141");
                m.put("N.Khallong", "2001004142");
                m.put("N.Mollen", "2001004143");
                m.put("N.Terakhong", "2001004145");
                m.put("New Salem", "2001004150");
                m.put("Ngaranphung", "2001004154");
                m.put("Ngarumphung", "2001004156");
                m.put("Ngoupikhong", "2001004157");
                m.put("Nongmaiching Tampak", "2001004159");
                m.put("Nongmaipal", "2001004160");
                m.put("Nongpok Centre", "2001004161");
                m.put("Nungkot Khuman", "2001004163");
                m.put("Nungkot Kom", "2001004164");
                m.put("Phailengjang", "2001004173");
                m.put("Phainom", "2001004174");
                m.put("Pheitaiching", "2001004176");
                m.put("Phouwaibi", "2001004178");
                m.put("Phunal Maring", "2001004179");
                m.put("Phungton", "2001004180");
                m.put("Poirou Tangkhul", "2001004181");
                m.put("Risophung", "2001004184");
                m.put("S Alimphai", "2001004185");
                m.put("S.Buongjang", "2001004188");
                m.put("S.Kanan", "2001004189");
                m.put("Sada Lungthar", "2001004193");
                m.put("Saichang", "2001004195");
                m.put("Sailent ( T )", "2001004199");
                m.put("Salam Patong", "2001004200");
                m.put("Samaching", "2001004201");
                m.put("Saman Tangkhul", "2001004202");
                m.put("Sandang Senba Kabui", "2001004204");
                m.put("Sangdangsenba Maring", "2001004205");
                m.put("Selhao Vaiphei", "2001004207");
                m.put("Somrei", "2001004264");
                m.put("Songphel", "2001004215");
                m.put("Soraland (T)", "2001004219");
                m.put("South Loushing", "2001004220");
                m.put("Tangkhul Khullen", "2001004227");
                m.put("Teraphai", "2001004229");
                m.put("Th.Salemphai", "2001004230");
                m.put("Thangja", "2001004232");
                m.put("Thangjingpokpi", "2001004233");
                m.put("Thayong", "2001004235");
                m.put("Tombi hangbu", "2001004242");
                m.put("Tongko Tangkhul", "2001004243");
                m.put("Tuisenphai", "2001004244");
                m.put("Uran Chiru", "2001004249");
                m.put("Vaiphei pakai", "2001004265");
                m.put("Waithou Chiru", "2001004252");
                m.put("Zeru Canan", "2001004259");
                return m;
            }
        }

        // ====== Block: KANGCHUP GELJANG TD BLOCK ======
        static final class KangchupGeljang {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("Bethel", "2001007106");
                m.put("Bijang", "2001007107");
                m.put("Ch.Ebenezer", "2001007121");
                m.put("Haibung", "2001007040");
                m.put("Haraothel", "2001007124");
                m.put("Irang Naga", "2001007110");
                m.put("K Songlung", "2001007047");
                m.put("K.Khomunnom", "2001007126");
                m.put("K.Patbung", "2001007127");
                m.put("K.Phaijol", "2001007128");
                m.put("K.Ponlen", "2001007129");
                m.put("Kangchup Chingkhong", "2001007130");
                m.put("Kangchup Chiru", "2001007111");
                m.put("Kangchup Geljang", "2001007112");
                m.put("Kangchup Khul Bangla", "2001007131");
                m.put("Kangchup Makhom", "2001007132");
                m.put("Kangchup Patjang", "2001007133");
                m.put("Kangchup Tuikun", "2001007113");
                m.put("Kharam Pallen", "2001007134");
                m.put("Kharam Thadoi", "2001007008");
                m.put("Kharam Vaiphei", "2001007135");
                m.put("Khoken", "2001007137");
                m.put("Khonglong Kabui", "2001007138");
                m.put("Khonglong Part (II)", "2001007054");
                m.put("Konsakhul", "2001007141");
                m.put("Kotlen", "2001007058");
                m.put("L. Hengjang", "2001007199");
                m.put("L.Champhai", "2001007142");
                m.put("L.Hengjol", "2001007143");
                m.put("L.Jangnoumphai", "2001007144");
                m.put("L.Munlui", "2001007145");
                m.put("L.Phaijang", "2001007146");
                m.put("L.Phaikot", "2001007147");
                m.put("Langdeibung", "2001007148");
                m.put("lanthunching1", "2001007149");
                m.put("Leilon Khunou", "2001007150");
                m.put("Leilon Vaiphei", "2001007151");
                m.put("Leimakhong", "2001007114");
                m.put("Leimakhong Mission Veng", "2001007152");
                m.put("Lhongchin", "2001007153");
                m.put("Longa Koireng", "2001007154");
                m.put("Lonjang", "2001007155");
                m.put("Machangram", "2001007156");
                m.put("Maha Kabui", "2001007157");
                m.put("Molhoi", "2001007117");
                m.put("N Boljang", "2001007159");
                m.put("N.C.Phai", "2001007160");
                m.put("Nakhojang", "2001007161");
                m.put("Namthanjang", "2001007162");
                m.put("Natop Kabui", "2001007163");
                m.put("New Keithelmanbi", "2001007164");
                m.put("P.Molding", "2001007168");
                m.put("Pholjang", "2001007118");
                m.put("Pongringlong", "2001007170");
                m.put("Ramgailong", "2001007171");
                m.put("S.Laijang", "2001007172");
                m.put("Saheibung", "2001007089");
                m.put("Salbung Sehjang", "2001007091");
                m.put("Salem Lhouthang", "2001007203");
                m.put("Samuk", "2001007176");
                m.put("Sinam Jonhol", "2001007178");
                m.put("Sinam khaithong1", "2001007179");
                m.put("Singda Kuki", "2001007119");
                m.put("Songjang Waphong", "2001007181");
                m.put("T.Laijang", "2001007182");
                m.put("Thangjing Chiru", "2001007184");
                m.put("Thanglong Molbung", "2001007120");
                m.put("Thingkhongjang", "2001007185");
                m.put("Veitum Khullen", "2001007104");
                m.put("vengnom", "2001007205");
                m.put("Waphong Inthan", "2001007189");
                return m;
            }
        }

        // ====== Block: KANGPOKPI ======
        static final class Kangpokpi {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                // Short version earlier (Ward No 1..9 etc). Expanded list below:
                m.put("12 miles IT road", "2001003001");
                m.put("Changoubung North", "2001003011");
                m.put("Changoubung South", "2001003134");
                m.put("Changoubung V.", "2001003012");
                m.put("Chawangkining", "2001003013");
                m.put("Chuchekhop(A) Irang Pt-II", "2001003014");
                m.put("Chuchekhop(B)", "2001003015");
                m.put("Chucheklop-(C)", "2001003016");
                m.put("Dahalthari Irang Pt-1", "2001003017");
                m.put("Daili", "2001003115");
                m.put("Dulen", "2001003019");
                m.put("Ghimethari", "2001003024");
                m.put("Haijang", "2001003031");
                m.put("Harup Maohing", "2001003034");
                m.put("Hengbung", "2001003136");
                m.put("Kailenjang", "2001003045");
                m.put("Kalapahar", "2007006019");
                m.put("Kalapahar(N)", "2001003158");
                m.put("Kangpokpi Ward No.1", "2001003137");
                m.put("Kangpokpi Ward No.10", "2001003146");
                m.put("Kangpokpi Ward No.11", "2001003147");
                m.put("Kangpokpi Ward No.12", "2001003148");
                m.put("Kangpokpi Ward No.13", "2001003149");
                m.put("Kangpokpi Ward No.14", "2001003150");
                m.put("Kangpokpi Ward No.2", "2001003138");
                m.put("Kangpokpi Ward No.3", "2001003139");
                m.put("Kangpokpi Ward No.4", "2001003140");
                m.put("Kangpokpi Ward No.5", "2001003141");
                m.put("Kangpokpi Ward No.6", "2001003142");
                m.put("Kangpokpi Ward No.7", "2001003143");
                m.put("Kangpokpi Ward No.8", "2001003144");
                m.put("Kangpokpi Ward No.9", "2001003145");
                m.put("Kazanga", "2001003117");
                m.put("Kongpao", "2001003119");
                m.put("Laikot", "2001003054");
                m.put("Lhangnom", "2001003132");
                m.put("Liyai Kalapahar", "2001003118");
                m.put("Lungphou", "2001003061");
                m.put("M/Ningthoupham", "2001003153");
                m.put("Mahika", "2001003120");
                m.put("Manedara Irang pt-1", "2001003065");
                m.put("Maohing Kuki", "2001003066");
                m.put("Maramei Naga", "2001003122");
                m.put("Mayangkhang", "2001003123");
                m.put("Mayangkhang Khunou", "2001003124");
                m.put("Phyapou", "2001003125");
                m.put("Poudel Basti", "2001003081");
                m.put("S.Khonomphai", "2001003133");
                m.put("School Praganda", "2001003091");
                m.put("SIMOL", "2001003093");
                m.put("Songpekjang", "2001003094");
                m.put("Songtun (J)", "2001003096");
                m.put("T.Aviall", "2001003097");
                m.put("Tagaramphung Yaikongpao", "2001003126");
                m.put("Tahar Basti", "2001003100");
                m.put("Tamuyon Khullen", "2001003154");
                m.put("Taphou Kuki", "2001003152");
                m.put("Thonglang Akutpa", "2001003129");
                m.put("Thonglang Atongba", "2001003130");
                m.put("Toribari", "2007006024");
                m.put("Toribari(N)", "2001003157");
                m.put("Tumnoupokpi", "2001003155");
                m.put("West Haipi", "2001003111");
                return m;
            }
        }

        // ====== Block: LHUNGTIN TD BLOCK ======
        static final class Lhungtin {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("B.Boljang", "2001004024");
                m.put("B.Khajang", "2001004025");
                m.put("C.Aisan", "2001004001");
                m.put("C.Kholen", "2001004031");
                m.put("Chanmol", "2001004038");
                m.put("Chingdai Khunou", "2001004040");
                m.put("Chonjang", "2001004043");
                m.put("Denglen", "2001004002");
                m.put("Dongsum", "2001004045");
                m.put("Gallam", "2001004049");
                m.put("Gampum", "2001004050");
                m.put("Gangpikon", "2001004052");
                m.put("Gwaltabi", "2001004057");
                m.put("H. Yangom", "2001004058");
                m.put("H.Langjol", "2001004266");
                m.put("J.K.Lunglhaimun(Dongsum Machet)", "2001004074");
                m.put("K.Khonom", "2001004077");
                m.put("K.Laitul(Waichei)", "2001004004");
                m.put("Khongbal Kuki", "2001004096");
                m.put("L Langjol (1)", "2001004100");
                m.put("L Phaijang (Phaijol)", "2001004101");
                m.put("Laikot Kom", "2001004106");
                m.put("Leplen", "2001004113");
                m.put("Lhangsom", "2001004114");
                m.put("Lhungtin", "2001004116");
                m.put("Maojang", "2001004124");
                m.put("Mojol", "2001004130");
                m.put("Mokang Ngamneh", "2001004131");
                m.put("Molcham", "2001004132");
                m.put("Moljang", "2001004133");
                m.put("Molkon", "2001004008");
                m.put("Molkon Bazar", "2001004009");
                m.put("Mongjang", "2001004137");
                m.put("Mongneljang", "2001004010");
                m.put("N.Moulhoiphai", "2001004144");
                m.put("N.Phailen", "2001004262");
                m.put("Naphai", "2001004011");
                m.put("Natjang", "2001004147");
                m.put("New Seibol", "2001004151");
                m.put("Ng.Phainom", "2001004152");
                m.put("Nomjang", "2001004158");
                m.put("P.Phaimol", "2001004169");
                m.put("Phaikon", "2001004171");
                m.put("Puleijang", "2001004017");
                m.put("S.Khonomphai", "2001004190");
                m.put("Samusong", "2001004203");
                m.put("Semuol", "2001004208");
                m.put("Sijang", "2001004209");
                m.put("Somphung", "2001004212");
                m.put("Songbem", "2001004213");
                m.put("Songphel Khullen", "2001004216");
                m.put("T.Gamnom", "2001004013");
                m.put("Tengkonphai", "2001004014");
                m.put("Thingphai", "2001004237");
                m.put("Tingpibung", "2001004241");
                m.put("Twichin", "2001004246");
                m.put("Twinomjang", "2001004247");
                m.put("Twisomjang", "2001004248");
                m.put("Urangpat", "2001004250");
                m.put("Walpabung", "2001004254");
                m.put("Yangnoi", "2001004257");
                m.put("yangnongphai", "2001004258");
                return m;
            }
        }

        // ====== Block: SAIKUL ======
        static final class Saikul {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("A.Geljang", "2001004018");
                m.put("A.Phainom", "2001004019");
                m.put("A.Twisomjang", "2001004020");
                m.put("Aigejang", "2001004021");
                m.put("Ankhumbung", "2001004022");
                m.put("Awanglonga Koireng", "2001004023");
                m.put("Bilei", "2001004029");
                m.put("Chalkot", "2001004033");
                m.put("Challouphai", "2001004034");
                m.put("Changsang", "2001004035");
                m.put("Changsang Yangmol (Changsang Machet)", "2001004036");
                m.put("Chingdai Khullen", "2001004039");
                m.put("Chingka Lambi", "2001004041");
                m.put("Chingmang", "2001004042");
                m.put("Dimjang", "2001004044");
                m.put("E.Mulam", "2001004046");
                m.put("Ekouphai", "2001004047");
                m.put("Ekpan Chingkha", "2001004048");
                m.put("Gangpijang", "2001004051");
                m.put("Govajang (Aigejang Machet)", "2001004054");
                m.put("Gunphaibung", "2001004055");
                m.put("Gunphaijang", "2001004056");
                m.put("H Mollen", "2001004260");
                m.put("H.Champhai", "2001004059");
                m.put("H.Gelmol", "2001004060");
                m.put("H.Khopibung", "2001004061");
                m.put("H.Moulhoi", "2001004062");
                m.put("Haibung", "2001004063");
                m.put("Haokip Veng", "2001004065");
                m.put("Happy Valley", "2001004067");
                m.put("Holbung", "2001004069");
                m.put("Ichaigojang", "2001004070");
                m.put("Ichailamlan", "2001004071");
                m.put("Khajang", "2001004091");
                m.put("Khamenlok", "2001004092");
                m.put("Khaochangbung", "2001004093");
                m.put("Khokon", "2001004095");
                m.put("Khongbal Tangkhul", "2001004097");
                m.put("Khongnangpokpi", "2001004005");
                m.put("L.Chajang", "2001004102");
                m.put("L.Lajang", "2001004103");
                m.put("L.Lhangjol", "2001004104");
                m.put("Lairok Kom", "2001004108");
                m.put("Leikot", "2001004261");
                m.put("Lhungjang", "2001004115");
                m.put("Luwangsangol(Ekpan Khullen)", "2001004118");
                m.put("M.Bunglung", "2001004007");
                m.put("M.Kholmun", "2001004119");
                m.put("Maibung Kom", "2001004120");
                m.put("Maibung Likli", "2001004121");
                m.put("Makokching", "2001004122");
                m.put("Makokching B", "2001004123");
                m.put("Mapao Christian (T)", "2001004125");
                m.put("Mapao Khullen (T)", "2001004126");
                m.put("Mapao Thangal", "2001004127");
                m.put("Molkot", "2001004135");
                m.put("Molvom Villge", "2001004136");
                m.put("Moulhoi", "2001004139");
                m.put("N.Chaljang", "2001004140");
                m.put("N.Zilphai", "2001004146");
                m.put("New Boljang", "2001004148");
                m.put("New Saikul", "2001004149");
                m.put("Ngakhapat", "2001004153");
                m.put("Ngarongchingjen", "2001004155");
                m.put("Nungka", "2001004162");
                m.put("Nuranthel Boljang", "2001004263");
                m.put("Nurathel", "2001004165");
                m.put("Old Boljang", "2001004166");
                m.put("P Geljang Villge", "2001004167");
                m.put("P.Khonomphai", "2001004168");
                m.put("Pangjang", "2001004012");
                m.put("Phaijang", "2001004170");
                m.put("Phaileng", "2001004172");
                m.put("Phainom (1)", "2001004175");
                m.put("Philen", "2001004177");
                m.put("Purum Khullen", "2001004182");
                m.put("Purum Likli", "2001004183");
                m.put("S. Boijang", "2001004186");
                m.put("S.Bolkot", "2001004187");
                m.put("S.Mongbung", "2001004191");
                m.put("S.Phaileng Kot", "2001004192");
                m.put("Sadu Koireng", "2001004194");
                m.put("Saijang", "2001004196");
                m.put("Saikul", "2001004197");
                m.put("Saikul Hill Town", "2001004198");
                m.put("Satang Kuki", "2001004206");
                m.put("Sinam Kom", "2001004211");
                m.put("Songjang", "2001004016");
                m.put("Songpehjang", "2001004214");
                m.put("Songphel L", "2001004217");
                m.put("Songthu Avenue", "2001004218");
                m.put("T Wakanphai", "2001004221");
                m.put("T.Gojang", "2001004222");
                m.put("T.Jordenphai (Thombol Machet)", "2001004223");
                m.put("T.Lamkajang", "2001004224");
                m.put("T.Thangkan", "2001004225");
                m.put("Tamaching Tkl", "2001004226");
                m.put("Taothang Veng", "2001004228");
                m.put("Thangal Sarung", "2001004231");
                m.put("Thangkanphai", "2001004234");
                m.put("Thingjang", "2001004236");
                m.put("Thingsat", "2001004238");
                m.put("Thombol", "2001004239");
                m.put("Thongnangpal", "2001004240");
                m.put("Tusam", "2001004015");
                m.put("Twichamphai", "2001004245");
                m.put("Utonglok", "2001004251");
                m.put("Wakan", "2001004253");
                m.put("Y. Langkhong", "2001004256");
                return m;
            }
        }

        // ====== Block: SAITU GAMPHAZOL ======
        static final class SaituGamphazol {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("A.Songpijang", "2001007001");
                m.put("Bethany", "2001007027");
                m.put("Bethsaida", "2001007191");
                m.put("Bolkot", "2001007028");
                m.put("Bongjol", "2001007029");
                m.put("C. Phaillen", "2001007207");
                m.put("C.Joujang", "2001007030");
                m.put("C.Munnom", "2001007031");
                m.put("Ch.Ollim", "2001007032");
                m.put("Chalbol", "2001007033");
                m.put("Chalbung", "2001007109");
                m.put("Dwarika Nepali", "2001007122");
                m.put("E.Sapormeina", "2001007003");
                m.put("G.Bunglen", "2001007034");
                m.put("G.Gelbung", "2001007004");
                m.put("G.Hengjang", "2001007123");
                m.put("G.Lhangjol", "2001007035");
                m.put("G.Lhangsom", "2001007193");
                m.put("G.Phalbung", "2001007036");
                m.put("G.Saparmeina", "2001007037");
                m.put("G.Songlung", "2001007005");
                m.put("G.Thangbuh", "2001007038");
                m.put("Gamgiphai", "2001007006");
                m.put("Goungaiphai", "2001007039");
                m.put("Haijol", "2001007041");
                m.put("Haiken", "2001007007");
                m.put("Hermon", "2001007042");
                m.put("HN Champhai", "2001007194");
                m.put("J.KHONUMJANG", "2001007044");
                m.put("J.Tuiphai", "2001007045");
                m.put("Joujang", "2001007046");
                m.put("Joulen", "2001007125");
                m.put("K. Phoipi", "2001007048");
                m.put("K.Lhangnom", "2001007196");
                m.put("K.Mollen", "2001007197");
                m.put("K.Orphanage", "2001007049");
                m.put("Kangchup Joute", "2001007050");
                m.put("Kanglatongbi Mollen", "2001007051");
                m.put("Khengjang", "2001007009");
                m.put("Khochal", "2001007052");
                m.put("Khokheng", "2001007010");
                m.put("Kholenphai", "2001007053");
                m.put("KHOLEP", "2001007011");
                m.put("Khongkhaijang", "2001007198");
                m.put("Khonomjang", "2001007055");
                m.put("Khunkhu Kuki", "2001007139");
                m.put("Khunkhu Naga", "2001007140");
                m.put("Kolpechang Kuki", "2001007057");
                m.put("Koubroleika(N)", "2001007209");
                m.put("Koubruleikha", "2007006005");
                m.put("L. MANGJOL", "2001007012");
                m.put("L.Gamngai (Munpi)", "2001007059");
                m.put("L.Gunphai", "2001007060");
                m.put("L.Khomunnom", "2001007061");
                m.put("L.Simol", "2001007062");
                m.put("Leikot", "2001007013");
                m.put("Lhangkichoi", "2001007014");
                m.put("LOWER KHENGJANG", "2001007064");
                m.put("M.Jangnomphai", "2001007065");
                m.put("M.Songpi", "2001007066");
                m.put("Makhan", "2001007116");
                m.put("Moljol", "2001007067");
                m.put("Molnoi", "2001007068");
                m.put("Mongjang", "2001007070");
                m.put("Mongpijang", "2001007071");
                m.put("Mongtung", "2001007200");
                m.put("Motbung", "2001007016");
                m.put("Motjang", "2001007072");
                m.put("Moulthang", "2001007073");
                m.put("N.Champhai", "2001007074");
                m.put("N.Khonom", "2001007017");
                m.put("N.N.KHONOMPHAI", "2001007201");
                m.put("N.Songlung", "2001007018");
                m.put("NEW SELSI", "2001007075");
                m.put("O.S.Tuinom", "2001007166");
                m.put("P.Champhai", "2001007167");
                m.put("PANGMOL", "2001007020");
                m.put("Pangsang", "2001007076");
                m.put("Phaijang", "2001007021");
                m.put("Phaijol", "2001007202");
                m.put("Phailengkot", "2001007077");
                m.put("Phoibi", "2001007022");
                m.put("Phoilen", "2001007078");
                m.put("S.Bollen", "2001007079");
                m.put("S.Boungpi", "2001007080");
                m.put("S.Buning", "2001007081");
                m.put("S.Chajang", "2001007082");
                m.put("S.Gamnomjang", "2001007083");
                m.put("S.Khajang", "2001007084");
                m.put("S.Lhangnom", "2001007085");
                m.put("S.Molcham", "2001007086");
                m.put("S.Silen", "2001007087");
                m.put("S.SIMTOL", "2001007088");
                m.put("S.Vengnom", "2001007208");
                m.put("Saitu", "2001007090");
                m.put("Saitu-Saparmeina", "2001007023");
                m.put("Santing", "2001007092");
                m.put("Saparmeina", "2001007093");
                m.put("sekmai leikhampokpi", "2001007177");
                m.put("Seloi", "2001007094");
                m.put("Selsi", "2001007204");
                m.put("Sipijang", "2001007095");
                m.put("Songjang Khullen", "2001007180");
                m.put("T.Chaljang", "2001007096");
                m.put("T.Kiplou", "2001007097");
                m.put("T.Simol", "2001007098");
                m.put("Taloulong", "2001007099");
                m.put("Taniulong Langka", "2001007183");
                m.put("Thingbongjang", "2001007100");
                m.put("Thomjang", "2001007024");
                m.put("TL.Jalenmol", "2001007102");
                m.put("Tuipajang", "2001007026");
                m.put("U.Saparmeina (Salem) village", "2001007103");
                m.put("W.Buning", "2001007105");
                m.put("Zalenbung", "2001007206");
                return m;
            }
        }

        // ====== Block: T. VAICHONG TD BLOCK ======
        static final class TVaichong {
            static Map<String, String> block() {
                Map<String, String> m = new LinkedHashMap<>();
                m.put("Bajagra", "2001003002");
                m.put("Beleijang", "2001003003");
                m.put("Bollen", "2001003004");
                m.put("Chalwa", "2001003009");
                m.put("Dhorigari", "2001003018");
                m.put("Gelbung", "2001003021");
                m.put("Geljang", "2001003022");
                m.put("Gelnal", "2001003023");
                m.put("Gorkha Harup", "2001003026");
                m.put("Gorkha Jhil", "2001003027");
                m.put("Gorkha Tapon", "2001003028");
                m.put("Govajang", "2001003029");
                m.put("Grouping Centre Songbhuphai", "2001003030");
                m.put("Haimol", "2001003032");
                m.put("Harup Khopi", "2001003033");
                m.put("Harup Naga 1", "2001003116");
                m.put("Haspukhri", "2001003035");
                m.put("Holjang", "2001003036");
                m.put("Khakare", "2001003046");
                m.put("Kharpani", "2001003047");
                m.put("Khomunnom", "2001003049");
                m.put("Kolgari", "2001003051");
                m.put("Kotlen", "2001003052");
                m.put("L. Sehjang", "2001003053");
                m.put("Lamchok Irang Pt-1", "2001003056");
                m.put("Leisang", "2001003057");
                m.put("Lhangjang", "2001003059");
                m.put("M. Wakotphai (Makui Mayankham)", "2001003062");
                m.put("M.Chaljang", "2001003063");
                m.put("Makui Ashang", "2001003064");
                m.put("Makui Naga", "2001003121");
                m.put("Maokot", "2001003067");
                m.put("N.Teikhang", "2001003071");
                m.put("Naibet", "2001003072");
                m.put("Panikheti", "2001003076");
                m.put("Phoikon", "2001003080");
                m.put("Rijalthar", "2001003082");
                m.put("S. phailen", "2001003086");
                m.put("Sangjang Pakang", "2001003090");
                m.put("Selsi", "2001003092");
                m.put("Songpijang", "2001003095");
                m.put("T.Khonomphai", "2001003098");
                m.put("Tapon Naga", "2001003127");
                m.put("Thaldara", "2001003101");
                m.put("Thebram", "2001003128");
                m.put("Thulochor", "2001003103");
                m.put("Timsina", "2001003104");
                m.put("Tuijang Vaichong", "2001003105");
                m.put("V Kholen", "2001003108");
                m.put("W Joupi", "2001003109");
                m.put("West Santing", "2001003112");
                return m;
            }
        }
    }
