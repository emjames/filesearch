import java.awt.*;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.GroupLayout.*;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import static javax.swing.GroupLayout.Alignment.CENTER;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Vector;

public class Homework6 extends JFrame {
    private DefaultListModel model;
    private DefaultListModel rmodel;
    // list to display the folders to search
    private JList list;
    private JList resultList;
    // buttons to control the actions
    private JButton remallbtn;
    private JButton addbtn;
    private JButton renbtn;
    private JButton delbtn;
    private JButton srcbtn;
    // labels
    private JTextField tfName;
    private JLabel lblName;
    private JLabel lblFolders;

    private JTable table;
    private JScrollPane tbScrollPane;
    // a list containing the list of values for the rows

    // file chooser
    private JFileChooser fc;

    // initiates the UI
    // calls all the other component creators
    public Homework6() {
        initUI();
        fc = new JFileChooser();
        // set file selection mode to directories only
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

    }

    // list containing all of the folders to search
    private void createList() {
        model = new DefaultListModel();
        model.addElement(System.getProperty("user.dir"));
        // model.addElement("B");
        // model.addElement("C");
        // model.addElement("D");

        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // when a list item is clicked
        list.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                // on double click, show a dialog to rename the item
                if (e.getClickCount() == 2) {
                    int index = list.locationToIndex(e.getPoint());
                    Object item = model.getElementAt(index);
                    String text = JOptionPane.showInputDialog("Rename item", item);
                    String newitem = null;
                    if (text != null) {
                        newitem = text.trim();
                    } else {
                        return;
                    }

                    if (!newitem.isEmpty()) {
                        model.remove(index);
                        model.add(index, newitem);
                        ListSelectionModel selmodel = list.getSelectionModel();
                        selmodel.setLeadSelectionIndex(index);
                    }
                }
            }
        });


    }

    private void createButtons() {

        remallbtn = new JButton("Remove All");
        addbtn = new JButton("Add");
        renbtn = new JButton("Rename");
        delbtn = new JButton("Delete");
        srcbtn = new JButton("Search");

        // add a new folder to the list
        addbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                // get the return value to make sure the user clicked CHOOSE
                int returnVal = fc.showOpenDialog(Homework6.this);

                if(returnVal == JFileChooser.APPROVE_OPTION) {
                    // get the folder the user selected
                    File file = fc.getSelectedFile();
                    // System.out.println("File name:" + file.getPath());
                    // add the folder to the list
                    model.addElement(file.getPath());
                } else {
                        System.out.println("Action cancelled");
                }

                // String text = JOptionPane.showInputDialog("Add a new item");
                // String item = null;
                //
                // if (text != null) {
                //     item = text.trim();
                // } else {
                //     return;
                // }
                //
                // if (!item.isEmpty()) {
                //     model.addElement(item);
                // }
            }
        });

        // delete the selected folder from the list
        delbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {

                ListSelectionModel selmodel = list.getSelectionModel();
                int index = selmodel.getMinSelectionIndex();
                if (index >= 0) {
                    model.remove(index);
                }
            }

        });

        // rename the selected folder
        renbtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                ListSelectionModel selmodel = list.getSelectionModel();
                int index = selmodel.getMinSelectionIndex();
                if (index == -1) {
                    return;
                }

                Object item = model.getElementAt(index);
                String text = JOptionPane.showInputDialog("Rename item", item);
                String newitem = null;

                if (text != null) {
                    newitem = text.trim();
                } else {
                    return;
                }

                if (!newitem.isEmpty()) {
                    model.remove(index);
                    model.add(index, newitem);
                }
            }
        });

        // clear the folder list
        remallbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                model.clear();
            }
        });


        srcbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Searching");
                // first empty the model
                rmodel.clear();
                // tfName.getText();
                ListModel folderListModel = list.getModel();
                for (int i = 0; i < folderListModel.getSize(); i++) {
                    System.out.println("E at " + i + ": " + folderListModel.getElementAt(i));
                    File f = new File(String.valueOf(folderListModel.getElementAt(i)));

                    File[] matchingFiles = f.listFiles(new FilenameFilter(){
                            public boolean accept(File dir, String name) {
                                // return name.contentEquals(tfName.getText());
                                return name.toLowerCase().contains(tfName.getText().toLowerCase());
                            }
                        });
                        System.out.println("Files found: "+ matchingFiles.length);
                        for(int j = 0; j < matchingFiles.length; j++){
                            rmodel.addElement(matchingFiles[j]);
                        }
                    }
                }
        });
    }

    private void createComponents() {
        lblFolders = new JLabel();
        lblFolders.setText("Folders: ");

        lblName = new JLabel();
        lblName.setText("Name: ");

        tfName = new JTextField(10);
        tfName.setSize(new Dimension(10, 10));
        tfName.setMaximumSize(tfName.getPreferredSize());
    }

    private void createTable(){
        // result list model
        rmodel = new DefaultListModel();
        resultList = new JList(rmodel);
        resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tbScrollPane = new JScrollPane(resultList);

    }

    private void initUI(){
        int width = 800;
        int height = 500;

        // call the helper functions to create the list, buttons and other components
        createList();
        createTable();
        createButtons();
        createComponents();

        // contains the UI
        JScrollPane scrollpane = new JScrollPane(list);
        scrollpane.setPreferredSize(new Dimension(150, 50));

        Container pane = getContentPane();
        GroupLayout gl = new GroupLayout(pane);
        pane.setLayout(gl);

        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);

        // horizontal alignment
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGroup(gl.createParallelGroup(CENTER)
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(lblName)
                        .addComponent(tfName)
                        .addComponent(srcbtn))
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(lblFolders)
                        .addComponent(scrollpane)
                        .addGroup(gl.createParallelGroup(CENTER)
                                .addComponent(addbtn)
                                .addComponent(renbtn)
                                .addComponent(delbtn)
                                .addComponent(remallbtn))
                    )
                    .addGroup(gl.createSequentialGroup()
                        .addComponent(tbScrollPane))
                )
        );

        // vertical alignment
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGroup(gl.createSequentialGroup()
                    .addGroup(gl.createParallelGroup(CENTER)
                        .addComponent(lblName)
                        .addComponent(tfName)
                        .addComponent(srcbtn))
                    .addGroup(gl.createParallelGroup(CENTER)
                    .addComponent(lblFolders)
                    .addComponent(scrollpane)
                    .addGroup(gl.createSequentialGroup()
                            .addComponent(addbtn)
                            .addComponent(renbtn)
                            .addComponent(delbtn)
                            .addComponent(remallbtn))
                    )
                    .addGroup(gl.createParallelGroup()
                        .addComponent(tbScrollPane))
                )
        );

        // keeps the button sizes linked (the same)
        gl.linkSize(addbtn, renbtn, delbtn, remallbtn, srcbtn);

        setSize(width, height);
        pack();

        setMinimumSize(new Dimension(width, height));
        setTitle("Homework6");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        Homework6 hw = new Homework6();
        hw.setVisible(true);
    }
}
