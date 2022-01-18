package com.patikadev.view;

import com.patikadev.helper.Config;
import com.patikadev.helper.Helper;
import com.patikadev.helper.Item;
import com.patikadev.model.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class OperatorGUI extends JFrame {

    private JPanel wrapper;
    private JTabbedPane tab_operator;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JButton btn_logout;
    private JPanel pnl_userList;
    private JScrollPane scrl_userList;
    private JTable tbl_userList;
    private JPanel pnl_userForm;
    private JTextField fld_userName;
    private JTextField fld_userUname;
    private JTextField fld_userPass;
    private JComboBox cmb_userType;
    private JButton btn_userAdd;
    private JTextField fld_userID;
    private JButton btn_userDelete;
    private JTextField fld_serchUserName;
    private JTextField fld_searchUserUname;
    private JComboBox cmb_searchUserType;
    private JButton btn_userSearch;
    private JPanel pnl_patikaList;
    private JScrollPane scrl_patikaList;
    private JTable tbl_patikaList;
    private JPanel pnl_patikaAdd;
    private JTextField fld_patikaName;
    private JButton btn_patikaAdd;
    private JPanel pnl_courseList;
    private JScrollPane scrl_courseList;
    private JTable tbl_courseList;
    private JPanel pnl_courseAdd;
    private JTextField fld_courseName;
    private JTextField fld_courseLang;
    private JComboBox cmb_coursePatika;
    private JComboBox cmb_courseUser;
    private JButton btn_courseAdd;
    private JPanel pnl_quizList;
    private JTable tbl_quizList;
    private DefaultTableModel mdl_userList;
    private Object[] row_userList;
    private DefaultTableModel mdl_patikaList;
    private Object[] row_patikaList;
    private JPopupMenu patikaMenu;
    private JPopupMenu quizMenu;
    private DefaultTableModel mdl_courseList;
    private Object[] row_courseList;
    private DefaultTableModel mdl_quizList;
    private Object[] row_quizList;

    private final Operator operator;

    public OperatorGUI(Operator operator) {
        this.operator = operator;

        add(wrapper);
        setSize(1000, 500);
        setLocation(Helper.screenCenter("x", getSize()), Helper.screenCenter("y", getSize()));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(Config.PROJECT_TITLE);
        setVisible(true);

        lbl_welcome.setText("Hoşgeldiniz: " + operator.getName());

        mdl_userList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                if(column == 0) return false;
                return super.isCellEditable(row, column);
            }
        };
        Object[] col_userList = {"ID", "Ad Soyad", "Kullanıcı Adı", "Şifre", "Üyelik Tipi"};
        mdl_userList.setColumnIdentifiers(col_userList);
        row_userList = new Object[col_userList.length];
        loadUserModel();

        tbl_userList.setModel(mdl_userList);
        tbl_userList.getTableHeader().setReorderingAllowed(false);

        tbl_userList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try{
                    String selectedUserID = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 0).toString();
                    fld_userID.setText(selectedUserID);
                }catch(Exception ex){}
            }
        });

        tbl_userList.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if(e.getType() == TableModelEvent.UPDATE){
                    int userID = Integer.parseInt(tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 0).toString());
                    String userName = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 1).toString();
                    String userUname = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 2).toString();
                    String userPass = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 3).toString();
                    String userType = tbl_userList.getValueAt(tbl_userList.getSelectedRow(), 4).toString();
                    if(User.update(userID, userName, userUname, userPass, userType)){
                        Helper.showMessage("done");
                    }

                    loadUserModel();
                    loadEducatorCombo();
                    loadCourseModel();
                }
            }
        });

        patikaMenu = new JPopupMenu();
        JMenuItem updateMenu = new JMenuItem("Güncelle");
        JMenuItem deleteMenu = new JMenuItem("Sil");
        patikaMenu.add(updateMenu);
        patikaMenu.add(deleteMenu);

        updateMenu.addActionListener(e -> {
            int selectedID = Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(), 0).toString());
            UpdatePatikaGUI updateGUI = new UpdatePatikaGUI(Patika.getFetch(selectedID));
            updateGUI.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }
            });
        });

        deleteMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedID = Integer.parseInt(tbl_patikaList.getValueAt(tbl_patikaList.getSelectedRow(), 0).toString());
                if(Patika.delete(selectedID)){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    loadCourseModel();
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        quizMenu = new JPopupMenu();
        JMenuItem deleteQuizMenu = new JMenuItem("Sil");
        quizMenu.add(deleteQuizMenu);

        deleteQuizMenu.addActionListener(e -> {
            if(Helper.confirm("sure")){
                int selectedID = Integer.parseInt(tbl_quizList.getValueAt(tbl_quizList.getSelectedRow(), 0).toString());
                if(Quiz.delete(selectedID)){
                    Helper.showMessage("done");
                    loadQuizModel();
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        mdl_patikaList = new DefaultTableModel();
        Object[] col_patikaList = {"ID", "Patika Adı"};
        mdl_patikaList.setColumnIdentifiers(col_patikaList);
        row_patikaList = new Object[col_patikaList.length];
        loadPatikaModel();

        tbl_patikaList.setModel(mdl_patikaList);
        tbl_patikaList.setComponentPopupMenu(patikaMenu);
        tbl_patikaList.getTableHeader().setReorderingAllowed(false);
        tbl_patikaList.getColumnModel().getColumn(0).setMaxWidth(75);

        tbl_patikaList.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Point point = e.getPoint();
                int selectedRow = tbl_patikaList.rowAtPoint(point);
                tbl_patikaList.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });

        mdl_courseList = new DefaultTableModel();
        Object[] col_courseList = {"ID", "Ders Adı", "Programlama Dili", "Patika", "Eğitmen"};
        mdl_courseList.setColumnIdentifiers(col_courseList);
        row_courseList = new Object[col_courseList.length];
        loadCourseModel();

        tbl_courseList.setModel(mdl_courseList);
        tbl_courseList.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_courseList.getTableHeader().setReorderingAllowed(false);

        mdl_quizList = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        Object[] col_quizList = {"ID", "KursID", "Ad"};
        mdl_quizList.setColumnIdentifiers(col_quizList);
        row_quizList = new Object[col_quizList.length];
        loadQuizModel();

        tbl_quizList.setModel(mdl_quizList);
        tbl_quizList.setComponentPopupMenu(quizMenu);
        tbl_quizList.getColumnModel().getColumn(0).setMaxWidth(75);
        tbl_quizList.getTableHeader().setReorderingAllowed(false);

        loadPatikaCombo();
        loadEducatorCombo();

        btn_userAdd.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_userName) || Helper.isFieldEmpty(fld_userUname) || Helper.isFieldEmpty(fld_userPass)){
                Helper.showMessage("fill");
            }else{
                String name = fld_userName.getText();
                String uname = fld_userUname.getText();
                String pass = fld_userPass.getText();
                String type = cmb_userType.getSelectedItem().toString();
                if(User.add(name, uname, pass, type)){
                    Helper.showMessage("done");
                    loadUserModel();
                    loadEducatorCombo();
                    fld_userName.setText(null);
                    fld_userUname.setText(null);
                    fld_userPass.setText(null);
                    cmb_userType.setSelectedIndex(0);
                }
            }
        });

        btn_userDelete.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_userID)){
                Helper.showMessage("fill");
            }else {
                if(Helper.confirm("sure")){
                    int userID = Integer.parseInt(fld_userID.getText());
                    if(User.delete(userID)){
                        Helper.showMessage("done");
                        loadUserModel();
                        loadEducatorCombo();
                        loadCourseModel();
                        fld_userID.setText(null);
                    }else{
                        Helper.showMessage("error");
                    }
                }
            }
        });

        btn_userSearch.addActionListener(e -> {
            String name = fld_serchUserName.getText();
            String uname = fld_searchUserUname.getText();
            String type = cmb_searchUserType.getSelectedItem().toString();
            String query = User.searchQuery(name, uname, type);

            loadUserModel(User.searchUserList(query));
        });

        btn_logout.addActionListener(e -> {
            dispose();
            LoginGUI login = new LoginGUI();
        });
        btn_patikaAdd.addActionListener(e -> {
            if(Helper.isFieldEmpty(fld_patikaName)){
                Helper.showMessage("fill");
            }else{
                if(Patika.add(fld_patikaName.getText())){
                    Helper.showMessage("done");
                    loadPatikaModel();
                    loadPatikaCombo();
                    fld_patikaName.setText(null);
                }else{
                    Helper.showMessage("error");
                }
            }
        });

        btn_courseAdd.addActionListener(e -> {
            Item patikaItem = (Item) cmb_coursePatika.getSelectedItem();
            Item userItem = (Item) cmb_courseUser.getSelectedItem();
            if(Helper.isFieldEmpty(fld_courseName) || Helper.isFieldEmpty(fld_courseLang)){
                Helper.showMessage("fill");
            }else{
                if(Course.add(userItem.getKey(), patikaItem.getKey(), fld_courseName.getText(), fld_courseLang.getText())){
                    Helper.showMessage("done");
                    loadCourseModel();
                    fld_courseLang.setText(null);
                    fld_courseName.setText(null);
                    cmb_coursePatika.setSelectedIndex(0);
                    cmb_courseUser.setSelectedIndex(0);
                }else{
                    Helper.showMessage("error");
                }
            }
        });
    }

    public void loadUserModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);

        int i;
        for(User obj : User.getList()){
            i = 0;
            row_userList[i++] = obj.getId();
            row_userList[i++] = obj.getName();
            row_userList[i++] = obj.getUname();
            row_userList[i++] = obj.getPass();
            row_userList[i++] = obj.getType();
            mdl_userList.addRow(row_userList);
        }
    }

    public void loadUserModel(ArrayList<User> list){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_userList.getModel();
        clearModel.setRowCount(0);
        int i;
        for(User obj : list){
            i = 0;
            row_userList[i++] = obj.getId();
            row_userList[i++] = obj.getName();
            row_userList[i++] = obj.getUname();
            row_userList[i++] = obj.getPass();
            row_userList[i++] = obj.getType();
            mdl_userList.addRow(row_userList);
        }
    }

    public void loadPatikaModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_patikaList.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Patika obj: Patika.getList()) {
            i = 0;
            row_patikaList[i++] = obj.getId();
            row_patikaList[i++] = obj.getName();
            mdl_patikaList.addRow(row_patikaList);
        }
    }

    public void loadCourseModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_courseList.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Course obj : Course.getList()){
            i = 0;
            row_courseList[i++] = obj.getId();
            row_courseList[i++] = obj.getName();
            row_courseList[i++] = obj.getLang();
            row_courseList[i++] = obj.getPatika().getName();
            row_courseList[i++] = obj.getEducator().getName();
            mdl_courseList.addRow(row_courseList);
        }
    }

    public void loadQuizModel(){
        DefaultTableModel clearModel = (DefaultTableModel) tbl_quizList.getModel();
        clearModel.setRowCount(0);
        int i;
        for(Quiz obj : Quiz.getList()){
            i = 0;
            row_quizList[i++] = obj.getId();
            row_quizList[i++] = obj.getCourse_id();
            row_quizList[i++] = obj.getName();
            mdl_quizList.addRow(row_quizList);
        }
    }

    public void loadPatikaCombo(){
        cmb_coursePatika.removeAllItems();
        for(Patika p : Patika.getList()){
            cmb_coursePatika.addItem(new Item(p.getId(), p.getName()));
        }
    }

    public void loadEducatorCombo(){
        cmb_courseUser.removeAllItems();
        for(User u : User.getListOnlyEducator()){
            cmb_courseUser.addItem(new Item(u.getId(), u.getName()));
        }
    }

    public static void main(String[] args) {
        Helper.setLayout();
        Operator operator = new Operator();
        operator.setId(1);
        operator.setName("Yunus Atahan");
        operator.setUname("Yunus");
        operator.setPass("12345");
        operator.setType("operator");

        OperatorGUI opGUI = new OperatorGUI(operator);
    }

}
