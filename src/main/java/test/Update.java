package test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

public class Update {


    public String getFullWay(String file) {     //поиск полного пути файла
        DBWorker dbWorker = new DBWorker();
        PreparedStatement st = null;
        PreparedStatement ps = null;
        boolean isElseDir = true;
        String query = "SELECT * FROM Files WHERE file_name = ?";
        String findEachDir = "Select parent_directory FROM Directory WHERE directory_name = ?";
        ArrayList<String> list = new ArrayList<>();
        list.add(file);
        try {
            st = dbWorker.getConnection().prepareStatement(query);
            st.setString(1,file);
            ResultSet rs = st.executeQuery();
            rs.next();
            list.add(rs.getString("parent_directory"));
            ps = dbWorker.getConnection().prepareStatement(findEachDir);
            while (isElseDir){
                isElseDir = false;
                ps.setString(1,list.get(list.size()-1));
                ResultSet resultSet = ps.executeQuery();
                while (resultSet.next()){
                    isElseDir = true;
                    list.add(resultSet.getString("parent_directory"));
                }
                }
            if (st != null)
                st.close();
            if (ps != null)
                ps.close();
            dbWorker.closeConnection();
    } catch (SQLException e) {
            e.printStackTrace();
        }
        String way = "...";
        for (int i = list.size()-1; i >= 0; i--) {
            if (list.get(i)!=null) {
                way += "/" + list.get(i);
            }
        }
        return way;
    }

    public void moveFiles(String from, String to){      //перемещение файлов
        DBWorker dbWorker = new DBWorker();
        String fromDir = "UPDATE Directory SET parent_directory = ? WHERE parent_directory = ?";
        String fromFiles = "UPDATE Files SET parent_directory = ? WHERE parent_directory = ?";
        PreparedStatement st = null;
        try {
            st = dbWorker.getConnection().prepareStatement(fromDir);
            for (int i = 0; i < 2; i++) {
                st.setString(1,to);
                st.setString(2,from);
                st.executeUpdate();
                st = dbWorker.getConnection().prepareStatement(fromFiles);
            }
            if (st != null)
                st.close();
            dbWorker.closeConnection();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void deleteDir(String from){     // удалить каталоги и файли из заданного каталога
        DBWorker dbWorker = new DBWorker();
        PreparedStatement ps = null;
        PreparedStatement psD = null;
        String select = "SELECT * FROM ?";
        String delete = "DELETE FROM ? WHERE parent_directory = ?";
        ArrayList<String> list = new ArrayList<>();
        list.add(from);
        try {
            ps = dbWorker.getConnection().prepareStatement(select);
            Iterator<String> it = list.iterator();
            for (int i = 0; i < 2; i++) {
                ps.setString(1, i == 0 ? "Direction" : "Files");
                ResultSet rs = ps.executeQuery();
                psD = dbWorker.getConnection().prepareStatement(delete);
                psD.setString(1, i == 0 ? "Direction" : "Files");
                while (it.hasNext()) {
                    String file = it.next();
                    while (rs.next()) {
                        if (file.equals(rs.getString("parent_directory"))) {
                            list.add(rs.getString("directory_name"));
                            psD.setString(2, rs.getString("directory_name"));
                            psD.executeUpdate();
                        }
                    }
                }
            }
        if(ps != null)
            ps.close();
        if(psD != null)
            psD.close();
        dbWorker.closeConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
