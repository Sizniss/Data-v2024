package kr.sizniss.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.OfflinePlayer;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Files {

    public static HashMap<OfflinePlayer, JsonObject> data = new HashMap<OfflinePlayer, JsonObject>();


    public Files() {
        createFolder(); // 플러그인 폴더 생성
    }


    // 폴더 생성 함수
    public static void createFolder() {
        File dirFolder = new File(Data.plugin.getDataFolder().toURI());
        if (!dirFolder.exists()) { // 경로 폴더가 없을 경우
            dirFolder.mkdirs(); // 경로 폴더 생성
        }
    }

    // 데이터 불러오기 함수
    public static void loadData(OfflinePlayer player) {
        File file = new File(Data.plugin.getDataFolder(), player.getUniqueId().toString() + ".json");
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write("{}");
                    fileWriter.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            Reader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            JsonParser parser = new JsonParser();
            data.put(player, parser.parse(reader).getAsJsonObject());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // 데이터 저장 함수
    public static void saveData(OfflinePlayer player) {
        File file = new File(Data.plugin.getDataFolder(), player.getUniqueId().toString() + ".json");
        if (!file.exists()) {
            try (FileWriter fileWriter = new FileWriter(file)) {
                fileWriter.write("{}");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(data.get(player), writer);
            writer.append(System.lineSeparator());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    // 데이터 제거 함수
    public static void removeData(OfflinePlayer player) {
        File file = new File(Data.plugin.getDataFolder(), player.getUniqueId().toString() + ".json");

        file.delete(); // 파일 제거
    }


    // 플레이어
    public static JsonObject getPlayer(OfflinePlayer player) {
        if (data.get(player) == null) { // 플레이어가 데이터에 없을 경우
            loadData(player); // 플레이어 데이터 불러오기
        }

        return data.get(player);
    }
    public static void addPlayer(OfflinePlayer player) {
        if (data.get(player) != null) { // 플레이어가 데이터에 있을 경우
            return; // 함수 반환
        }

        loadData(player); // 플레이어 데이터 불러오기
    }
    public static void removePlayer(OfflinePlayer player) {
        if (data.get(player) != null) { // 플레이어가 데이터에 있을 경우
            data.remove(player); // 플레이어 데이터 제거
        }
        
        removeData(player); // 플레이어 데이터 제거
    }

    // 접속
    public static JsonObject getConnection(OfflinePlayer player) {
        if (getPlayer(player).get("Connection") == null) {
            getPlayer(player).add("Connection", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Connection").getAsJsonObject();
    }
    public static void removeConnection(OfflinePlayer player) {
        getPlayer(player).add("Connection", null);

        saveData(player);
    }
    public static Date getLastDate(OfflinePlayer player) {
        if (getConnection(player).get("LastDate") == null) {
            getConnection(player).addProperty("LastDate", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            saveData(player);
        }

        Date date;
        try {
            String str = getConnection(player).get("LastDate").getAsString();
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
        } catch(ParseException e) {
            date = null;
        }

        return date;
    }
    public static void setLastDate(OfflinePlayer player, Date date) {
        String dateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        getConnection(player).addProperty("LastDate", dateStr);

        saveData(player);
    }
    public static void removeLastDate(OfflinePlayer player) {
        getConnection(player).add("LastDate", null);

        saveData(player);
    }
    public static int getCount(OfflinePlayer player) {
        if (getConnection(player).get("Count") == null) {
            getConnection(player).addProperty("Count", 0);

            saveData(player);
        }

        return getConnection(player).get("Count").getAsInt();
    }
    public static void setCount(OfflinePlayer player, int value) {
        getConnection(player).addProperty("Count", value);

        saveData(player);
    }
    public static void addCount(OfflinePlayer player, int value) {
        int currentCount = getCount(player);

        getConnection(player).addProperty("Count", currentCount + value);

        saveData(player);
    }
    public static void subtractCount(OfflinePlayer player, int value) {
        int currentCount = getCount(player);

        getConnection(player).addProperty("Count", currentCount - value);

        saveData(player);
    }
    public static void removeCount(OfflinePlayer player) {
        getConnection(player).add("Count", null);

        saveData(player);
    }
    public static String getUserName(OfflinePlayer player) {
        if (getConnection(player).get("UserName") == null) {
            getConnection(player).addProperty("UserName", player.getName());

            saveData(player);
        }

        return getConnection(player).get("UserName").getAsString();
    }
    public static void setUserName(OfflinePlayer player, String name) {
        getConnection(player).addProperty("UserName", name);

        saveData(player);
    }
    public static void removeUserName(OfflinePlayer player) {
        getConnection(player).add("UserName", null);

        saveData(player);
    }
    public static String getIp(OfflinePlayer player) {
        if (getConnection(player).get("Ip") == null) {
            getConnection(player).addProperty("Ip", player.getPlayer().getAddress().getHostName());

            saveData(player);
        }

        return getConnection(player).get("Ip").getAsString();
    }
    public static void setIp(OfflinePlayer player, String ip) {
        getConnection(player).addProperty("Ip", ip);

        saveData(player);
    }
    public static void removeIp(OfflinePlayer player) {
        getConnection(player).add("Ip", null);

        saveData(player);
    }
    public static String getUserNameList(OfflinePlayer player) {
        if (getConnection(player).get("UserNameList") == null) {
            getConnection(player).addProperty("UserNameList", player.getName());

            saveData(player);
        }

        return getConnection(player).get("UserNameList").getAsString();
    }
    public static void addUserNameList(OfflinePlayer player, String name) {
        String currentNameList = getUserNameList(player);

        String[] list = currentNameList.split(", ");
        boolean isContained = false;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(name)) {
                isContained = true;
                break;
            }
        }

        if (!isContained) {
            currentNameList = currentNameList + ", " + name;

            getConnection(player).addProperty("UserNameList", currentNameList);

            saveData(player);
        }
    }
    public static void removeUserNameList(OfflinePlayer player, String name) {
        String currentNameList = getUserNameList(player);

        if (currentNameList.contains(name)) {
            String[] nameList = currentNameList.split(", ");

            String newNameList = "";
            for (int i = 0; i < nameList.length; i++) {
                if (nameList[i].equals(name)) {
                    continue;
                }

                newNameList = newNameList + nameList[i];
                if (i < nameList.length - 1) {
                    newNameList = newNameList + ", ";
                }
            }

            getConnection(player).addProperty("UserNameList", newNameList);

            saveData(player);
        }
    }
    public static void removeUserNameList(OfflinePlayer player) {
        getConnection(player).add("UserNameList", null);

        saveData(player);
    }
    public static String getIpList(OfflinePlayer player) {
        if (getConnection(player).get("IpList") == null) {
            getConnection(player).addProperty("IpList", player.getPlayer().getAddress().getHostName());

            saveData(player);
        }

        return getConnection(player).get("IpList").getAsString();
    }
    public static void addIpList(OfflinePlayer player, String ip) {
        String currentIpList = getIpList(player);

        String[] list = currentIpList.split(", ");
        boolean isContained = false;
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(ip)) {
                isContained = true;
                break;
            }
        }

        if (!isContained) {
            currentIpList = currentIpList + ", " + ip;

            getConnection(player).addProperty("IpList", currentIpList);

            saveData(player);
        }
    }
    public static void removeIpList(OfflinePlayer player, String ip) {
        String currentIpList = getIpList(player);

        if (currentIpList.contains(ip)) {
            String[] ipList = currentIpList.split(", ");

            String newIpList = "";
            for (int i = 0; i < ipList.length; i++) {
                if (ipList[i].equals(ip)) {
                    continue;
                }

                newIpList = newIpList + ipList[i];
                if (i < ipList.length - 1) {
                    newIpList = newIpList + ", ";
                }
            }

            getConnection(player).addProperty("IpList", newIpList);

            saveData(player);
        }
    }
    public static void removeIpList(OfflinePlayer player) {
        getConnection(player).add("IpList", null);

        saveData(player);
    }

    // 돈
    public static JsonObject getMoney(OfflinePlayer player) {
        if (getPlayer(player).get("Money") == null) {
            getPlayer(player).add("Money", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Money").getAsJsonObject();
    }
    public static void removeMoney(OfflinePlayer player) {
        getPlayer(player).add("Money", null);

        saveData(player);
    }
    public static int getMoney(OfflinePlayer player, String money) {
        if (getMoney(player).get(money) == null) {
            getMoney(player).addProperty(money, 0);

            saveData(player);
        }

        return getMoney(player).get(money).getAsInt();
    }
    public static void setMoney(OfflinePlayer player, String money, int value) {
        getMoney(player).addProperty(money, value);

        saveData(player);
    }
    public static void addMoney(OfflinePlayer player, String money, int value) {
        int currentMoney = getMoney(player, money);

        getMoney(player).addProperty(money, currentMoney + value);

        saveData(player);
    }
    public static void subtractMoney(OfflinePlayer player, String money, int value) {
        int currentMoney = getMoney(player, money);

        getMoney(player).addProperty(money, currentMoney - value);

        saveData(player);
    }
    public static void removeMoney(OfflinePlayer player, String money) {
        getMoney(player).add(money, null);

        saveData(player);
    }

    // 기록
    public static JsonObject getRecord(OfflinePlayer player) {
        if (getPlayer(player).get("Record") == null) {
            getPlayer(player).add("Record", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Record").getAsJsonObject();
    }
    public static void removeRecord(OfflinePlayer player) {
        getPlayer(player).add("Record", null);

        saveData(player);
    }
    public static int getRecord(OfflinePlayer player, String record) {
        if (getRecord(player).get(record) == null) {
            getRecord(player).addProperty(record, 0);

            saveData(player);
        }

        return getRecord(player).get(record).getAsInt();
    }
    public static void setRecord(OfflinePlayer player, String record, int value) {
        getRecord(player).addProperty(record, value);

        saveData(player);
    }
    public static void addRecord(OfflinePlayer player, String record, int value) {
        int currentRecord = getRecord(player, record);

        getRecord(player).addProperty(record, currentRecord + value);

        saveData(player);
    }
    public static void subtractRecord(OfflinePlayer player, String record, int value) {
        int currentRecord = getRecord(player, record);

        getRecord(player).addProperty(record, currentRecord - value);

        saveData(player);
    }
    public static void removeRecord(OfflinePlayer player, String record) {
        getRecord(player).add(record, null);

        saveData(player);
    }

    // 플레이 횟수
    public static JsonObject getPlayCount(OfflinePlayer player) {
        if (getPlayer(player).get("PlayCount") == null) {
            getPlayer(player).add("PlayCount", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("PlayCount").getAsJsonObject();
    }
    public static void removePlayCount(OfflinePlayer player) {
        getPlayer(player).add("PlayCount", null);

        saveData(player);
    }
    public static JsonObject getClassPlayCount(OfflinePlayer player) {
        if (getPlayCount(player).get("Class") == null) {
            getPlayCount(player).add("Class", new JsonObject());

            saveData(player);
        }

        return getPlayCount(player).get("Class").getAsJsonObject();
    }
    public static void removeClassPlayCount(OfflinePlayer player) {
        getPlayCount(player).add("Class", null);

        saveData(player);
    }
    public static int getClassPlayCount(OfflinePlayer player, String kind) {
        if (getClassPlayCount(player).get(kind) == null) {
            getClassPlayCount(player).addProperty(kind, 0);

            saveData(player);
        }

        return getClassPlayCount(player).get(kind).getAsInt();
    }
    public static void setClassPlayCount(OfflinePlayer player, String kind, int value) {
        getClassPlayCount(player).addProperty(kind, value);

        saveData(player);
    }
    public static void addClassPlayCount(OfflinePlayer player, String kind, int value) {
        int currentPlayCount = getClassPlayCount(player, kind);

        getClassPlayCount(player).addProperty(kind, currentPlayCount + value);

        saveData(player);
    }
    public static void subtractClassPlayCount(OfflinePlayer player, String kind, int value) {
        int currentPlayCount = getClassPlayCount(player, kind);

        getClassPlayCount(player).addProperty(kind, currentPlayCount - value);

        saveData(player);
    }
    public static void removeClassPlayCount(OfflinePlayer player, String kind) {
        getClassPlayCount(player).add(kind, null);

        saveData(player);
    }
    public static JsonObject getTypePlayCount(OfflinePlayer player) {
        if (getPlayCount(player).get("Type") == null) {
            getPlayCount(player).add("Type", new JsonObject());

            saveData(player);
        }

        return getPlayCount(player).get("Type").getAsJsonObject();
    }
    public static void removeTypePlayCount(OfflinePlayer player) {
        getPlayCount(player).add("Type", null);

        saveData(player);
    }
    public static int getTypePlayCount(OfflinePlayer player, String type) {
        if (getTypePlayCount(player).get(type) == null) {
            getTypePlayCount(player).addProperty(type, 0);

            saveData(player);
        }

        return getTypePlayCount(player).get(type).getAsInt();
    }
    public static void setTypePlayCount(OfflinePlayer player, String type, int value) {
        getTypePlayCount(player).addProperty(type, value);

        saveData(player);
    }
    public static void addTypePlayCount(OfflinePlayer player, String type, int value) {
        int currentPlayCount = getTypePlayCount(player, type);

        getTypePlayCount(player).addProperty(type, currentPlayCount + value);

        saveData(player);
    }
    public static void subtractTypePlayCount(OfflinePlayer player, String type, int value) {
        int currentPlayCount = getTypePlayCount(player, type);

        getTypePlayCount(player).addProperty(type, currentPlayCount - value);

        saveData(player);
    }
    public static void removeTypePlayCount(OfflinePlayer player, String type) {
        getTypePlayCount(player).add(type, null);

        saveData(player);
    }
    public static JsonObject getWeaponPlayCount(OfflinePlayer player) {
        if (getPlayCount(player).get("Weapon") == null) {
            getPlayCount(player).add("Weapon", new JsonObject());

            saveData(player);
        }

        return getPlayCount(player).get("Weapon").getAsJsonObject();
    }
    public static void removeWeaponPlayCount(OfflinePlayer player) {
        getPlayCount(player).add("Weapon", null);

        saveData(player);
    }
    public static JsonObject getMainWeaponPlayCount(OfflinePlayer player) {
        if (getWeaponPlayCount(player).get("Main") == null) {
            getWeaponPlayCount(player).add("Main", new JsonObject());

            saveData(player);
        }

        return getWeaponPlayCount(player).get("Main").getAsJsonObject();
    }
    public static void removeMainWeaponPlayCount(OfflinePlayer player) {
        getWeaponPlayCount(player).add("Main", null);

        saveData(player);
    }
    public static int getMainWeaponPlayCount(OfflinePlayer player, String weapon) {
        if (getMainWeaponPlayCount(player).get(weapon) == null) {
            getMainWeaponPlayCount(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getMainWeaponPlayCount(player).get(weapon).getAsInt();
    }
    public static void setMainWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        getMainWeaponPlayCount(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addMainWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getMainWeaponPlayCount(player, weapon);

        getMainWeaponPlayCount(player).addProperty(weapon, currentPlayCount + value);

        saveData(player);
    }
    public static void subtractMainWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getMainWeaponPlayCount(player, weapon);

        getMainWeaponPlayCount(player).addProperty(weapon, currentPlayCount - value);

        saveData(player);
    }
    public static void removeMainWeaponPlayCount(OfflinePlayer player, String weapon) {
        getMainWeaponPlayCount(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getSubWeaponPlayCount(OfflinePlayer player) {
        if (getWeaponPlayCount(player).get("Sub") == null) {
            getWeaponPlayCount(player).add("Sub", new JsonObject());

            saveData(player);
        }

        return getWeaponPlayCount(player).get("Sub").getAsJsonObject();
    }
    public static void removeSubWeaponPlayCount(OfflinePlayer player) {
        getWeaponPlayCount(player).add("Sub", null);

        saveData(player);
    }
    public static int getSubWeaponPlayCount(OfflinePlayer player, String weapon) {
        if (getSubWeaponPlayCount(player).get(weapon) == null) {
            getSubWeaponPlayCount(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getSubWeaponPlayCount(player).get(weapon).getAsInt();
    }
    public static void setSubWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        getSubWeaponPlayCount(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addSubWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getSubWeaponPlayCount(player, weapon);

        getSubWeaponPlayCount(player).addProperty(weapon, currentPlayCount + value);

        saveData(player);
    }
    public static void subtractSubWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getSubWeaponPlayCount(player, weapon);

        getSubWeaponPlayCount(player).addProperty(weapon, currentPlayCount - value);

        saveData(player);
    }
    public static void removeSubWeaponPlayCount(OfflinePlayer player, String weapon) {
        getSubWeaponPlayCount(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getMeleeWeaponPlayCount(OfflinePlayer player) {
        if (getWeaponPlayCount(player).get("Melee") == null) {
            getWeaponPlayCount(player).add("Melee", new JsonObject());

            saveData(player);
        }

        return getWeaponPlayCount(player).get("Melee").getAsJsonObject();
    }
    public static void removeMeleeWeaponPlayCount(OfflinePlayer player) {
        getWeaponPlayCount(player).add("Melee", null);

        saveData(player);
    }
    public static int getMeleeWeaponPlayCount(OfflinePlayer player, String weapon) {
        if (getMeleeWeaponPlayCount(player).get(weapon) == null) {
            getMeleeWeaponPlayCount(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getMeleeWeaponPlayCount(player).get(weapon).getAsInt();
    }
    public static void setMeleeWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        getMeleeWeaponPlayCount(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addMeleeWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getMeleeWeaponPlayCount(player, weapon);

        getMeleeWeaponPlayCount(player).addProperty(weapon, currentPlayCount + value);

        saveData(player);
    }
    public static void subtractMeleeWeaponPlayCount(OfflinePlayer player, String weapon, int value) {
        int currentPlayCount = getMeleeWeaponPlayCount(player, weapon);

        getMeleeWeaponPlayCount(player).addProperty(weapon, currentPlayCount - value);

        saveData(player);
    }
    public static void removeMeleeWeaponPlayCount(OfflinePlayer player, String weapon) {
        getMeleeWeaponPlayCount(player).add(weapon, null);

        saveData(player);
    }

    // 상자
    public static JsonObject getBox(OfflinePlayer player) {
        if (getPlayer(player).get("Box") == null) {
            getPlayer(player).add("Box", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Box").getAsJsonObject();
    }
    public static void removeBox(OfflinePlayer player) {
        getPlayer(player).add("Box", null);

        saveData(player);
    }
    public static int getBox(OfflinePlayer player, String box) {
        if (getBox(player).get(box) == null) {
            getBox(player).addProperty(box, 0);

            saveData(player);
        }

        return getBox(player).get(box).getAsInt();
    }
    public static void setBox(OfflinePlayer player, String box, int value) {
        getBox(player).addProperty(box, value);

        saveData(player);
    }
    public static void addBox(OfflinePlayer player, String box, int value) {
        int currentBox = getBox(player, box);

        getBox(player).addProperty(box, currentBox + value);

        saveData(player);
    }
    public static void subtractBox(OfflinePlayer player, String box, int value) {
        int currentBox = getBox(player, box);

        getBox(player).addProperty(box, currentBox - value);

        saveData(player);
    }
    public static void removeBox(OfflinePlayer player, String box) {
        getBox(player).add(box, null);

        saveData(player);
    }

    // 상자 개봉 횟수
    public static JsonObject getBoxOpenCount(OfflinePlayer player) {
        if (getPlayer(player).get("BoxOpenCount") == null) {
            getPlayer(player).add("BoxOpenCount", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("BoxOpenCount").getAsJsonObject();
    }
    public static void removeBoxOpenCount(OfflinePlayer player) {
        getPlayer(player).add("BoxOpenCount", null);

        saveData(player);
    }
    public static int getBoxOpenCount(OfflinePlayer player, String box) {
        if (getBoxOpenCount(player).get(box) == null) {
            getBoxOpenCount(player).addProperty(box, 0);

            saveData(player);
        }

        return getBoxOpenCount(player).get(box).getAsInt();
    }
    public static void setBoxOpenCount(OfflinePlayer player, String box, int value) {
        getBoxOpenCount(player).addProperty(box, value);

        saveData(player);
    }
    public static void addBoxOpenCount(OfflinePlayer player, String box, int value) {
        int currentBox = getBoxOpenCount(player, box);

        getBoxOpenCount(player).addProperty(box, currentBox + value);

        saveData(player);
    }
    public static void subtractBoxOpenCount(OfflinePlayer player, String box, int value) {
        int currentBox = getBoxOpenCount(player, box);

        getBoxOpenCount(player).addProperty(box, currentBox - value);

        saveData(player);
    }
    public static void removeBoxOpenCount(OfflinePlayer player, String box) {
        getBoxOpenCount(player).add(box, null);

        saveData(player);
    }

    // 조각
    public static JsonObject getPiece(OfflinePlayer player) {
        if (getPlayer(player).get("Piece") == null) {
            getPlayer(player).add("Piece", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Piece").getAsJsonObject();
    }
    public static void removePiece(OfflinePlayer player) {
        getPlayer(player).add("Piece", null);

        saveData(player);
    }
    public static int getPiece(OfflinePlayer player, String grade) {
        if (getPiece(player).get(grade) == null) {
            getPiece(player).addProperty(grade, 0);

            saveData(player);
        }

        return getPiece(player).get(grade).getAsInt();
    }
    public static void setPiece(OfflinePlayer player, String grade, int value) {
        getPiece(player).addProperty(grade, value);

        saveData(player);
    }
    public static void addPiece(OfflinePlayer player, String grade, int value) {
        int currentPiece = getPiece(player, grade);

        getPiece(player).addProperty(grade, currentPiece + value);

        saveData(player);
    }
    public static void subtractPiece(OfflinePlayer player, String grade, int value) {
        int currentPiece = getPiece(player, grade);

        getPiece(player).addProperty(grade, currentPiece - value);

        saveData(player);
    }
    public static void removePiece(OfflinePlayer player, String grade) {
        getPiece(player).add(grade, null);

        saveData(player);
    }

    // 무기
    public static JsonObject getWeapon(OfflinePlayer player) {
        if (getPlayer(player).get("Weapon") == null) {
            getPlayer(player).add("Weapon", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Weapon").getAsJsonObject();
    }
    public static void removeWeapon(OfflinePlayer player) {
        getPlayer(player).add("Weapon", null);

        saveData(player);
    }
    public static JsonObject getMainWeapon(OfflinePlayer player) {
        if (getWeapon(player).get("Main") == null) {
            getWeapon(player).add("Main", new JsonObject());

            saveData(player);
        }

        return getWeapon(player).get("Main").getAsJsonObject();
    }
    public static boolean getMainWeapon(OfflinePlayer player, String weapon) {
        if (getMainWeapon(player).get(weapon) == null) {
            getMainWeapon(player).addProperty(weapon, false);

            saveData(player);
        }

        return getMainWeapon(player).get(weapon).getAsBoolean();
    }
    public static void setMainWeapon(OfflinePlayer player, String weapon, boolean value) {
        getMainWeapon(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void removeMainWeapon(OfflinePlayer player, String weapon) {
        getMainWeapon(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getSubWeapon(OfflinePlayer player) {
        if (getWeapon(player).get("Sub") == null) {
            getWeapon(player).add("Sub", new JsonObject());

            saveData(player);
        }

        return getWeapon(player).get("Sub").getAsJsonObject();
    }
    public static boolean getSubWeapon(OfflinePlayer player, String weapon) {
        if (getSubWeapon(player).get(weapon) == null) {
            getSubWeapon(player).addProperty(weapon, false);

            saveData(player);
        }

        return getSubWeapon(player).get(weapon).getAsBoolean();
    }
    public static void setSubWeapon(OfflinePlayer player, String weapon, boolean value) {
        getSubWeapon(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void removeSubWeapon(OfflinePlayer player, String weapon) {
        getSubWeapon(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getMeleeWeapon(OfflinePlayer player) {
        if (getWeapon(player).get("Melee") == null) {
            getWeapon(player).add("Melee", new JsonObject());

            saveData(player);
        }

        return getWeapon(player).get("Melee").getAsJsonObject();
    }
    public static boolean getMeleeWeapon(OfflinePlayer player, String weapon) {
        if (getMeleeWeapon(player).get(weapon) == null) {
            getMeleeWeapon(player).addProperty(weapon, false);

            saveData(player);
        }

        return getMeleeWeapon(player).get(weapon).getAsBoolean();
    }
    public static void setMeleeWeapon(OfflinePlayer player, String weapon, boolean value) {
        getMeleeWeapon(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void removeMeleeWeapon(OfflinePlayer player, String weapon) {
        getWeapon(player).add("Melee", null);

        saveData(player);
    }

    // 무기 레벨
    public static JsonObject getWeaponLevel(OfflinePlayer player) {
        if (getPlayer(player).get("WeaponLevel") == null) {
            getPlayer(player).add("WeaponLevel", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("WeaponLevel").getAsJsonObject();
    }
    public static void removeWeaponLevel(OfflinePlayer player) {
        getPlayer(player).add("WeaponLevel", null);

        saveData(player);
    }
    public static JsonObject getMainWeaponLevel(OfflinePlayer player) {
        if (getWeaponLevel(player).get("Main") == null) {
            getWeaponLevel(player).add("Main", new JsonObject());

            saveData(player);
        }

        return getWeaponLevel(player).get("Main").getAsJsonObject();
    }
    public static int getMainWeaponLevel(OfflinePlayer player, String weapon) {
        if (getMainWeaponLevel(player).get(weapon) == null) {
            getMainWeaponLevel(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getMainWeaponLevel(player).get(weapon).getAsInt();
    }
    public static void setMainWeaponLevel(OfflinePlayer player, String weapon, int value) {
        getMainWeaponLevel(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addMainWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getMainWeaponLevel(player, weapon);

        getMainWeaponLevel(player).addProperty(weapon, currentLevel + value);

        saveData(player);
    }
    public static void subtractMainWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getMainWeaponLevel(player, weapon);

        getMainWeaponLevel(player).addProperty(weapon, currentLevel - value);

        saveData(player);
    }
    public static void removeMainWeaponLevel(OfflinePlayer player, String weapon) {
        getMainWeaponLevel(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getSubWeaponLevel(OfflinePlayer player) {
        if (getWeaponLevel(player).get("Sub") == null) {
            getWeaponLevel(player).add("Sub", new JsonObject());

            saveData(player);
        }

        return getWeaponLevel(player).get("Sub").getAsJsonObject();
    }
    public static int getSubWeaponLevel(OfflinePlayer player, String weapon) {
        if (getSubWeaponLevel(player).get(weapon) == null) {
            getSubWeaponLevel(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getSubWeaponLevel(player).get(weapon).getAsInt();
    }
    public static void setSubWeaponLevel(OfflinePlayer player, String weapon, int value) {
        getSubWeaponLevel(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addSubWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getSubWeaponLevel(player, weapon);

        getSubWeaponLevel(player).addProperty(weapon, currentLevel + value);

        saveData(player);
    }
    public static void subtractSubWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getSubWeaponLevel(player, weapon);

        getSubWeaponLevel(player).addProperty(weapon, currentLevel - value);

        saveData(player);
    }
    public static void removeSubWeaponLevel(OfflinePlayer player, String weapon) {
        getSubWeaponLevel(player).add(weapon, null);

        saveData(player);
    }
    public static JsonObject getMeleeWeaponLevel(OfflinePlayer player) {
        if (getWeaponLevel(player).get("Melee") == null) {
            getWeaponLevel(player).add("Melee", new JsonObject());

            saveData(player);
        }

        return getWeaponLevel(player).get("Melee").getAsJsonObject();
    }
    public static int getMeleeWeaponLevel(OfflinePlayer player, String weapon) {
        if (getMeleeWeaponLevel(player).get(weapon) == null) {
            getMeleeWeaponLevel(player).addProperty(weapon, 0);

            saveData(player);
        }

        return getMeleeWeaponLevel(player).get(weapon).getAsInt();
    }
    public static void setMeleeWeaponLevel(OfflinePlayer player, String weapon, int value) {
        getMeleeWeaponLevel(player).addProperty(weapon, value);

        saveData(player);
    }
    public static void addMeleeWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getMeleeWeaponLevel(player, weapon);

        getMeleeWeaponLevel(player).addProperty(weapon, currentLevel + value);

        saveData(player);
    }
    public static void subtractMeleeWeaponLevel(OfflinePlayer player, String weapon, int value) {
        int currentLevel = getMeleeWeaponLevel(player, weapon);

        getMeleeWeaponLevel(player).addProperty(weapon, currentLevel - value);

        saveData(player);
    }
    public static void removeMeleeWeaponLevel(OfflinePlayer player, String weapon) {
        getMeleeWeaponLevel(player).add(weapon, null);

        saveData(player);
    }

    // 코디
    public static JsonObject getCodi(OfflinePlayer player) {
        if (getPlayer(player).get("Codi") == null) {
            getPlayer(player).add("Codi", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Codi").getAsJsonObject();
    }
    public static JsonObject getBorderColor(OfflinePlayer player) {
        if (getCodi(player).get("BorderColor") == null) {
            getCodi(player).add("BorderColor", new JsonObject());

            saveData(player);
        }

        return getCodi(player).get("BorderColor").getAsJsonObject();
    }
    public static boolean getBorderColor(OfflinePlayer player, String color) {
        if (getBorderColor(player).get(color) == null) {
            getBorderColor(player).addProperty(color, false);

            saveData(player);
        }

        return getBorderColor(player).get(color).getAsBoolean();
    }
    public static void setBorderColor(OfflinePlayer player, String color, boolean value) {
        getBorderColor(player).addProperty(color, value);

        saveData(player);
    }
    public static JsonObject getPrefixColor(OfflinePlayer player) {
        if (getCodi(player).get("PrefixColor") == null) {
            getCodi(player).add("PrefixColor", new JsonObject());

            saveData(player);
        }

        return getCodi(player).get("PrefixColor").getAsJsonObject();
    }
    public static boolean getPrefixColor(OfflinePlayer player, String color) {
        if (getPrefixColor(player).get(color) == null) {
            getPrefixColor(player).addProperty(color, false);

            saveData(player);
        }

        return getPrefixColor(player).get(color).getAsBoolean();
    }
    public static void setPrefixColor(OfflinePlayer player, String color, boolean value) {
        getPrefixColor(player).addProperty(color, value);

        saveData(player);
    }
    public static JsonObject getNickColor(OfflinePlayer player) {
        if (getCodi(player).get("NickColor") == null) {
            getCodi(player).add("NickColor", new JsonObject());

            saveData(player);
        }

        return getCodi(player).get("NickColor").getAsJsonObject();
    }
    public static boolean getNickColor(OfflinePlayer player, String color) {
        if (getNickColor(player).get(color) == null) {
            getNickColor(player).addProperty(color, false);

            saveData(player);
        }

        return getNickColor(player).get(color).getAsBoolean();
    }
    public static void setNickColor(OfflinePlayer player, String color, boolean value) {
        getNickColor(player).addProperty(color, value);

        saveData(player);
    }
    public static JsonObject getChatColor(OfflinePlayer player) {
        if (getCodi(player).get("ChatColor") == null) {
            getCodi(player).add("ChatColor", new JsonObject());

            saveData(player);
        }

        return getCodi(player).get("ChatColor").getAsJsonObject();
    }
    public static boolean getChatColor(OfflinePlayer player, String color) {
        if (getChatColor(player).get(color) == null) {
            getChatColor(player).addProperty(color, false);

            saveData(player);
        }

        return getChatColor(player).get(color).getAsBoolean();
    }
    public static void setChatColor(OfflinePlayer player, String color, boolean value) {
        getChatColor(player).addProperty(color, value);

        saveData(player);
    }

    // 스테이터스
    public static JsonObject getStatus(OfflinePlayer player) {
        if (getPlayer(player).get("Status") == null) {
            getPlayer(player).add("Status", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Status").getAsJsonObject();
    }
    public static JsonObject getClassStatus(OfflinePlayer player) {
        if (getStatus(player).get("Class") == null) {
            getStatus(player).add("Class", new JsonObject());

            saveData(player);
        }

        return getStatus(player).get("Class").getAsJsonObject();
    }
    public static JsonObject getClassStatus(OfflinePlayer player, String kind) {
        if (getClassStatus(player).get(kind) == null) {
            getClassStatus(player).add(kind, new JsonObject());

            saveData(player);
        }

        return getClassStatus(player).get(kind).getAsJsonObject();
    }
    public static int getClassStatus(OfflinePlayer player, String kind, String status) {
        if (getClassStatus(player, kind).get(status) == null) {
            getClassStatus(player, kind).addProperty(status, 0);

            saveData(player);
        }

        return getClassStatus(player, kind).get(status).getAsInt();
    }
    public static void setClassStatus(OfflinePlayer player, String kind, String status, int value) {
        getClassStatus(player, kind).addProperty(status, value);

        saveData(player);
    }
    public static void addClassStatus(OfflinePlayer player, String kind, String status, int value) {
        int currentStatus = getClassStatus(player, kind, status);

        getClassStatus(player, kind).addProperty(status, currentStatus + value);

        saveData(player);
    }
    public static void subtractClassStatus(OfflinePlayer player, String kind, String status, int value) {
        int currentStatus = getClassStatus(player, kind, status);

        getClassStatus(player, kind).addProperty(status, currentStatus - value);

        saveData(player);
    }
    public static void removeClassStatus(OfflinePlayer player, String kind, String status) {
        getClassStatus(player, kind).add(status, null);

        saveData(player);
    }
    public static JsonObject getTypeStatus(OfflinePlayer player) {
        if (getStatus(player).get("Type") == null) {
            getStatus(player).add("Type", new JsonObject());

            saveData(player);
        }

        return getStatus(player).get("Type").getAsJsonObject();
    }
    public static JsonObject getTypeStatus(OfflinePlayer player, String type) {
        if (getTypeStatus(player).get(type) == null) {
            getTypeStatus(player).add(type, new JsonObject());

            saveData(player);
        }

        return getTypeStatus(player).get(type).getAsJsonObject();
    }
    public static int getTypeStatus(OfflinePlayer player, String type, String status) {
        if (getTypeStatus(player, type).get(status) == null) {
            getTypeStatus(player, type).addProperty(status, 0);

            saveData(player);
        }

        return getTypeStatus(player, type).get(status).getAsInt();
    }
    public static void setTypeStatus(OfflinePlayer player, String type, String status, int value) {
        getTypeStatus(player, type).addProperty(status, value);

        saveData(player);
    }
    public static void addTypeStatus(OfflinePlayer player, String type, String status, int value) {
        int currentStatus = getTypeStatus(player, type, status);

        getTypeStatus(player, type).addProperty(status, currentStatus + value);

        saveData(player);
    }
    public static void subtractTypeStatus(OfflinePlayer player, String type, String status, int value) {
        int currentStatus = getTypeStatus(player, type, status);

        getTypeStatus(player, type).addProperty(status, currentStatus - value);

        saveData(player);
    }
    public static void removeTypeStatus(OfflinePlayer player, String type, String status) {
        getTypeStatus(player, type).add(status, null);

        saveData(player);
    }

    // 옵션
    public static JsonObject getOption(OfflinePlayer player) {
        if (getPlayer(player).get("Option") == null) {
            getPlayer(player).add("Option", new JsonObject());

            saveData(player);
        }

        return getPlayer(player).get("Option").getAsJsonObject();
    }
    public static JsonObject getOptionCodi(OfflinePlayer player) {
        if (getOption(player).get("Codi") == null) {
            getOption(player).add("Codi", new JsonObject());

            saveData(player);
        }

        return getOption(player).get("Codi").getAsJsonObject();
    }
    public static String getOptionCodi(OfflinePlayer player, String color) {
        if (getOptionCodi(player).get(color) == null) {
            getOptionCodi(player).addProperty(color, "White");

            saveData(player);
        }

        return getOptionCodi(player).get(color).getAsString();
    }
    public static void setOptionCodi(OfflinePlayer player, String color, String value) {
        getOptionCodi(player).addProperty(color, value);

        saveData(player);
    }
    public static JsonObject getOptionWeapon(OfflinePlayer player) {
        if (getOption(player).get("Weapon") == null) {
            getOption(player).add("Weapon", new JsonObject());

            saveData(player);
        }

        return getOption(player).get("Weapon").getAsJsonObject();
    }
    public static String getOptionWeapon(OfflinePlayer player, String kind) {
        if (getOptionWeapon(player).get(kind) == null) {
            if (kind.equals("Main")) {
                getOptionWeapon(player).addProperty(kind, "MP5");
            } else if (kind.equals("Sub")) {
                getOptionWeapon(player).addProperty(kind, "HK_USP");
            } else if (kind.equals("Melee")) {
                getOptionWeapon(player).addProperty(kind, "씰_나이프");
            }

            saveData(player);
        }

        return getOptionWeapon(player).get(kind).getAsString();
    }
    public static void setOptionWeapon(OfflinePlayer player, String kind, String weapon) {
        getOptionWeapon(player).addProperty(kind, weapon);

        saveData(player);
    }
    public static String getOptionClass(OfflinePlayer player) {
        if (getOption(player).get("Class") == null) {
            getOption(player).addProperty("Class", "기갑병");

            saveData(player);
        }

        return getOption(player).get("Class").getAsString();
    }
    public static void setOptionClass(OfflinePlayer player, String classStr) {
        getOption(player).addProperty("Class", classStr);

        saveData(player);
    }
    public static String getOptionType(OfflinePlayer player) {
        if (getOption(player).get("Type") == null) {
            getOption(player).addProperty("Type", "네크로맨서");

            saveData(player);
        }

        return getOption(player).get("Type").getAsString();
    }
    public static void setOptionType(OfflinePlayer player, String typeStr) {
        getOption(player).addProperty("Type", typeStr);

        saveData(player);
    }
}
