/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import controller.BookingController;
import controller.CustomerController;
import controller.MovieController;
import controller.StudioController;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Booking;
import model.Customer;
import model.Movie;
import model.Studio;
import model.User;
/**
 *
 * @author Arbi Wiratama
 */
public class BookingForm extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(BookingForm.class.getName());
    private BookingController bookingController;
    private CustomerController customerController;
    private MovieController movieController;
    private StudioController studioController;
    private int selectedId = -1;
    private int currentPage = 1;
    private final int recordsPerPage = 10;
    private String selectedSeat = "";
    private JButton selectedButton = null;
    private User user;
    /**
     * Creates new form BookingForm
     */
    
    public BookingForm() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        bookingController = new BookingController();
        customerController = new CustomerController();
        movieController = new MovieController();
        studioController = new StudioController();
        loadCustomer();
        loadMovie();
        loadStudio();
        loadTable();
        updateSummary();
        
        if (jComboBoxStudio.getSelectedItem() != null) {
        createSeat((Studio) jComboBoxStudio.getSelectedItem());
        }
    }
    
    public BookingForm(User user) {
        this.user = user;
        this();
    }
    
    private void loadTable() {
        try {
        DefaultTableModel model = (DefaultTableModel) jTableBooking.getModel();
        model.setRowCount(0);
        ArrayList<Booking> bookings = bookingController.pagination(currentPage, recordsPerPage);
        for (Booking booking : bookings) {
            Object[] row = {
                booking.getBookingID(),
                booking.getCustomer().getName(),
                booking.getMovie().getTitle(),
                booking.getStudio().getStudioName(),
                booking.getSeatNumber(),
                booking.getBookingDate(),
                booking.getShowTime(),
                booking.getTotalPrice()
            };
            model.addRow(row);
            jTableBooking.getColumnModel().getColumn(0).setMinWidth(0);
            jTableBooking.getColumnModel().getColumn(0).setMaxWidth(0);
            jTableBooking.getColumnModel().getColumn(0).setWidth(0);
            jTableBooking.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
    
    private void loadCustomer() {
        DefaultComboBoxModel<Customer> model = new DefaultComboBoxModel<>();
        ArrayList<Customer> customers = customerController.getAll();
        for (Customer customer : customers) {
            model.addElement(customer);
        }
        jComboBoxCustomer.setModel(model);
    }
    
    private void loadMovie(){
        DefaultComboBoxModel<Movie> model = new DefaultComboBoxModel<>();
        ArrayList<Movie> movies = movieController.getAll();
        for (Movie movie : movies) {
            model.addElement(movie);
        }
        jComboBoxMovie.setModel(model);
    }
    
    private void loadStudio(){
        DefaultComboBoxModel<Studio> model = new DefaultComboBoxModel<>();
        ArrayList<Studio> studios = studioController.getAll();
        for (Studio studio : studios) {
            model.addElement(studio);
        }
        jComboBoxStudio.setModel(model);
    }
    
    private void clearForm() {
        selectedId = -1;
        jComboBoxCustomer.setSelectedIndex(-1);
        jComboBoxMovie.setSelectedIndex(-1);
        jComboBoxStudio.setSelectedIndex(-1);
        
        selectedSeat = "";
        selectedButton = null;

        jDateChooser1.setDate(null);
        jComboBoxTime.setSelectedIndex(-1);

        jTableBooking.clearSelection();

        updateSummary();
    }
    
    private void searchData() {

        DefaultTableModel model = (DefaultTableModel) jTableBooking.getModel();
        model.setRowCount(0);

        ArrayList<Booking> bookings =
                bookingController.search(jTextFieldSearch.getText());

        for (Booking booking : bookings) {

            Object[] row = {
                booking.getBookingID(),
                booking.getCustomer().getName(),
                booking.getMovie().getTitle(),
                booking.getStudio().getStudioName(),
                booking.getBookingDate(),
                booking.getShowTime(),
                booking.getSeatNumber(),
                booking.getTotalPrice()
            };

            model.addRow(row);
        }
    }
    
    private void createSeat(Studio studio) {
        jPanelSeatGrid.removeAll();
        selectedSeat = "";
        selectedButton = null;
        jLabelSeat.setText("-");
        int rows = studio.getCapacity() / 10;
        char rowLetter = 'A';
        ArrayList<String> bookedSeats = new ArrayList<>();

        try {
            if (jDateChooser1.getDate() != null&& jComboBoxTime.getSelectedItem() != null) {
                bookedSeats = bookingController.getBookedSeats(
                        studio.getStudioID(),
                        new java.sql.Date(jDateChooser1.getDate().getTime()),
                        java.sql.Time.valueOf(jComboBoxTime.getSelectedItem().toString()));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 1; j <= 10; j++) {
                String seat = rowLetter + "" + j;
                JButton btnSeat = new JButton(seat);
                if (bookedSeats.contains(seat)) {
                    btnSeat.setBackground(Color.RED);
                    btnSeat.setEnabled(false);
                } else {
                    btnSeat.setBackground(new Color(144, 238, 144));
                }
                btnSeat.setFocusPainted(false);
                btnSeat.addActionListener(e -> {
                    if (selectedButton != null) {
                        selectedButton.setBackground(new Color(144, 238, 144));
                    }
                    selectedButton = btnSeat;
                    btnSeat.setBackground(Color.CYAN);
                    selectedSeat = btnSeat.getText();
                    jLabelSeat.setText(selectedSeat);
                    updateSummary();
                });
                jPanelSeatGrid.add(btnSeat);
            }
            rowLetter++;
        }
        jPanelSeatGrid.revalidate();
        jPanelSeatGrid.repaint();
    }
    
    private void refreshSeat() {
        Studio studio = (Studio) jComboBoxStudio.getSelectedItem();
        if (studio == null) {
            return;
        }
        createSeat(studio);
    }
    
    private void updateSummary() {

        Movie movie = (Movie) jComboBoxMovie.getSelectedItem();
        Studio studio = (Studio) jComboBoxStudio.getSelectedItem();
        Customer customer = (Customer) jComboBoxCustomer.getSelectedItem();

        jLabelMovie.setText(movie == null ? "-" : movie.getTitle());
        jLabelStudio.setText(studio == null ? "-" : studio.getStudioName());
        jLabelCustomer.setText(customer == null ? "-" : customer.getName());
        jLabelSeat.setText(selectedSeat.isEmpty() ? "-" : selectedSeat);

        if (jDateChooser1.getDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            jLabelDate.setText(sdf.format(jDateChooser1.getDate()));
        } else {
            jLabelDate.setText("-");
        }

        if (jComboBoxTime.getSelectedItem() != null) {
            jLabelTime.setText(jComboBoxTime.getSelectedItem().toString());
        } else {
            jLabelTime.setText("-");
        }

        double price = 0;
        double total = 0;

        if (studio != null) {
            price = studio.getTicketPrice();
            total = price;
        }

        if (customer != null&& customer.getMembership().equalsIgnoreCase("Member")) {
            total = price * 0.9;   // Diskon 10%

        }

        jLabelPrice.setText("Rp " + String.format("%,.0f", price));
        jLabelTotal.setText("Rp " + String.format("%,.0f", total));
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanelBackground = new javax.swing.JPanel();
        jPanelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanelContent = new javax.swing.JPanel();
        jPanelInput = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jComboBoxTime = new javax.swing.JComboBox<>();
        jComboBoxCustomer = new javax.swing.JComboBox<>();
        jComboBoxMovie = new javax.swing.JComboBox<>();
        jComboBoxStudio = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jDateChooser1 = new com.toedter.calendar.JDateChooser();
        jPanelCenter = new javax.swing.JPanel();
        jPanelTable = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableBooking = new javax.swing.JTable();
        jTextFieldSearch = new javax.swing.JTextField();
        jButtonSearch = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jButtonNext = new javax.swing.JButton();
        jPanelSeat = new javax.swing.JPanel();
        jPanelScreen = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanelSeatGrid = new javax.swing.JPanel();
        jPanelSummary = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabelCustomer = new javax.swing.JLabel();
        jLabelMovie = new javax.swing.JLabel();
        jLabelStudio = new javax.swing.JLabel();
        jLabelSeat = new javax.swing.JLabel();
        jLabelDate = new javax.swing.JLabel();
        jLabelTotal = new javax.swing.JLabel();
        jLabelTime = new javax.swing.JLabel();
        jLabelPrice = new javax.swing.JLabel();
        jButtonBack = new javax.swing.JButton();
        jButtonClear = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonSave = new javax.swing.JButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BookingManagement");

        jPanelBackground.setBackground(new java.awt.Color(18, 18, 18));
        jPanelBackground.setLayout(new java.awt.BorderLayout());

        jPanelHeader.setBackground(new java.awt.Color(30, 30, 30));
        jPanelHeader.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(255, 51, 51)));
        jPanelHeader.setPreferredSize(new java.awt.Dimension(1776, 60));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText(" Booking Management");
        jLabel1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 0, 51)));

        javax.swing.GroupLayout jPanelHeaderLayout = new javax.swing.GroupLayout(jPanelHeader);
        jPanelHeader.setLayout(jPanelHeaderLayout);
        jPanelHeaderLayout.setHorizontalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel1)
                .addContainerGap(1256, Short.MAX_VALUE))
        );
        jPanelHeaderLayout.setVerticalGroup(
            jPanelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelHeaderLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanelBackground.add(jPanelHeader, java.awt.BorderLayout.PAGE_START);

        jPanelContent.setBackground(new java.awt.Color(18, 18, 18));
        jPanelContent.setLayout(new java.awt.BorderLayout());

        jPanelInput.setBackground(new java.awt.Color(30, 30, 30));
        jPanelInput.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(60, 60, 60)));
        jPanelInput.setPreferredSize(new java.awt.Dimension(1776, 110));
        jPanelInput.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Customer");
        jPanelInput.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, -1, 20));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Movie");
        jPanelInput.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, -1, 20));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Show Time");
        jPanelInput.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(1200, 20, -1, 20));

        jComboBoxTime.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "10:00:00", "13:00:00", "16:00:00", "19:00:00", "21:00:00" }));
        jComboBoxTime.addActionListener(this::jComboBoxTimeActionPerformed);
        jPanelInput.add(jComboBoxTime, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 60, 150, -1));

        jComboBoxCustomer.addActionListener(this::jComboBoxCustomerActionPerformed);
        jPanelInput.add(jComboBoxCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 150, -1));

        jComboBoxMovie.addActionListener(this::jComboBoxMovieActionPerformed);
        jPanelInput.add(jComboBoxMovie, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 60, 150, -1));

        jComboBoxStudio.addActionListener(this::jComboBoxStudioActionPerformed);
        jPanelInput.add(jComboBoxStudio, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 60, 150, -1));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Date");
        jPanelInput.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 20, -1, 20));

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Studio");
        jPanelInput.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 20, -1, 20));

        jDateChooser1.addPropertyChangeListener(this::jDateChooser1PropertyChange);
        jPanelInput.add(jDateChooser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 60, 150, -1));

        jPanelContent.add(jPanelInput, java.awt.BorderLayout.PAGE_START);

        jPanelCenter.setLayout(new java.awt.BorderLayout());

        jPanelTable.setBackground(new java.awt.Color(30, 30, 30));
        jPanelTable.setPreferredSize(new java.awt.Dimension(1542, 220));

        jTableBooking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Booking ID", "Customer", "Movie", "Studio", "Seat", "Date", "Time", "Total"
            }
        ));
        jTableBooking.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableBookingMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTableBooking);

        jTextFieldSearch.setText("Searching");
        jTextFieldSearch.addActionListener(this::jTextFieldSearchActionPerformed);
        jTextFieldSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextFieldSearchKeyReleased(evt);
            }
        });

        jButtonSearch.setBackground(new java.awt.Color(255, 0, 51));
        jButtonSearch.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSearch.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSearch.setText("Search");
        jButtonSearch.addActionListener(this::jButtonSearchActionPerformed);

        jButtonPrev.setBackground(new java.awt.Color(255, 0, 51));
        jButtonPrev.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonPrev.setForeground(new java.awt.Color(255, 255, 255));
        jButtonPrev.setText("<< Prev");
        jButtonPrev.addActionListener(this::jButtonPrevActionPerformed);

        jButtonNext.setBackground(new java.awt.Color(255, 0, 51));
        jButtonNext.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonNext.setForeground(new java.awt.Color(255, 255, 255));
        jButtonNext.setText("Next >>");
        jButtonNext.addActionListener(this::jButtonNextActionPerformed);

        javax.swing.GroupLayout jPanelTableLayout = new javax.swing.GroupLayout(jPanelTable);
        jPanelTable.setLayout(jPanelTableLayout);
        jPanelTableLayout.setHorizontalGroup(
            jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTableLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanelTableLayout.createSequentialGroup()
                        .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButtonSearch)
                        .addGap(1223, 1223, 1223))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanelTableLayout.createSequentialGroup()
                            .addComponent(jButtonPrev)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButtonNext))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 1485, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        jPanelTableLayout.setVerticalGroup(
            jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTableLayout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButtonSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButtonPrev)
                    .addComponent(jButtonNext))
                .addGap(9, 9, 9))
        );

        jPanelCenter.add(jPanelTable, java.awt.BorderLayout.PAGE_END);

        jPanelSeat.setBackground(new java.awt.Color(30, 30, 30));
        jPanelSeat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60)));
        jPanelSeat.setPreferredSize(new java.awt.Dimension(1000, 374));
        jPanelSeat.setLayout(new java.awt.BorderLayout());

        jPanelScreen.setBackground(new java.awt.Color(30, 30, 30));
        jPanelScreen.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(60, 60, 60)));
        jPanelScreen.setPreferredSize(new java.awt.Dimension(998, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("SCREEN");
        jLabel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(255, 0, 51)));

        javax.swing.GroupLayout jPanelScreenLayout = new javax.swing.GroupLayout(jPanelScreen);
        jPanelScreen.setLayout(jPanelScreenLayout);
        jPanelScreenLayout.setHorizontalGroup(
            jPanelScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScreenLayout.createSequentialGroup()
                .addGap(456, 456, 456)
                .addComponent(jLabel2)
                .addContainerGap(475, Short.MAX_VALUE))
        );
        jPanelScreenLayout.setVerticalGroup(
            jPanelScreenLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelScreenLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanelSeat.add(jPanelScreen, java.awt.BorderLayout.PAGE_START);

        jPanelSeatGrid.setBackground(new java.awt.Color(30, 30, 30));
        jPanelSeatGrid.setLayout(new java.awt.GridLayout(6, 10, 10, 10));
        jPanelSeat.add(jPanelSeatGrid, java.awt.BorderLayout.CENTER);

        jPanelCenter.add(jPanelSeat, java.awt.BorderLayout.LINE_START);

        jPanelSummary.setBackground(new java.awt.Color(30, 30, 30));
        jPanelSummary.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(60, 60, 60)));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Booking Summary");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Customer");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Movie          ");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Studio");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Seat             ");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Date            ");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Time ");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Price            ");

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Total           ");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText(":");

        jLabel18.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText(":");

        jLabel19.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText(":");

        jLabel20.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText(":");

        jLabel21.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText(":");

        jLabel22.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText(":");

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText(":");

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText(":");

        jLabelCustomer.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelCustomer.setForeground(new java.awt.Color(255, 255, 255));

        jLabelMovie.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelMovie.setForeground(new java.awt.Color(255, 255, 255));

        jLabelStudio.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelStudio.setForeground(new java.awt.Color(255, 255, 255));

        jLabelSeat.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelSeat.setForeground(new java.awt.Color(255, 255, 255));

        jLabelDate.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(255, 255, 255));

        jLabelTotal.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTotal.setForeground(new java.awt.Color(255, 255, 255));

        jLabelTime.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(255, 255, 255));

        jLabelPrice.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabelPrice.setForeground(new java.awt.Color(255, 255, 255));

        jButtonBack.setBackground(new java.awt.Color(255, 0, 51));
        jButtonBack.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonBack.setForeground(new java.awt.Color(255, 255, 255));
        jButtonBack.setText("Back");
        jButtonBack.addActionListener(this::jButtonBackActionPerformed);

        jButtonClear.setBackground(new java.awt.Color(255, 0, 51));
        jButtonClear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonClear.setForeground(new java.awt.Color(255, 255, 255));
        jButtonClear.setText("Clear");
        jButtonClear.addActionListener(this::jButtonClearActionPerformed);

        jButtonDelete.setBackground(new java.awt.Color(255, 0, 51));
        jButtonDelete.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonDelete.setForeground(new java.awt.Color(255, 255, 255));
        jButtonDelete.setText("Delete");
        jButtonDelete.addActionListener(this::jButtonDeleteActionPerformed);

        jButtonSave.setBackground(new java.awt.Color(255, 0, 51));
        jButtonSave.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButtonSave.setForeground(new java.awt.Color(255, 255, 255));
        jButtonSave.setText("Save");
        jButtonSave.addActionListener(this::jButtonSaveActionPerformed);

        javax.swing.GroupLayout jPanelSummaryLayout = new javax.swing.GroupLayout(jPanelSummary);
        jPanelSummary.setLayout(jPanelSummaryLayout);
        jPanelSummaryLayout.setHorizontalGroup(
            jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSummaryLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11)
                        .addComponent(jLabel10)
                        .addComponent(jLabel13)
                        .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelSeat, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelStudio, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabelMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(jLabelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(jPanelSummaryLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(jButtonBack, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jButtonClear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(jButtonSave, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanelSummaryLayout.setVerticalGroup(
            jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelSummaryLayout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(jLabel3))
                    .addComponent(jLabelMovie, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                        .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel19)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelSummaryLayout.createSequentialGroup()
                        .addComponent(jLabelCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelStudio, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel13)
                        .addComponent(jLabel20))
                    .addComponent(jLabelSeat, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel14)
                        .addComponent(jLabel21))
                    .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanelSummaryLayout.createSequentialGroup()
                        .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(jLabel22))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17)
                            .addComponent(jLabel24)))
                    .addGroup(jPanelSummaryLayout.createSequentialGroup()
                        .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabelPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 27, Short.MAX_VALUE)
                .addGroup(jPanelSummaryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButtonSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonClear, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonBack, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButtonDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(24, 24, 24))
        );

        jPanelCenter.add(jPanelSummary, java.awt.BorderLayout.CENTER);

        jPanelContent.add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelBackground.add(jPanelContent, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanelBackground, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        try {
            if (selectedId == -1) {
                JOptionPane.showMessageDialog(this,
                        "Please select booking data first!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this booking?",
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                bookingController.delete(selectedId);
                JOptionPane.showMessageDialog(this,
                        "Booking deleted successfully!");
                clearForm();
                loadTable();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jTextFieldSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextFieldSearchActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextFieldSearchActionPerformed

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        if(currentPage > 1){
        currentPage--;
        loadTable();
        }
    }//GEN-LAST:event_jButtonPrevActionPerformed

    private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSaveActionPerformed
        try {
            Booking booking = new Booking();
            booking.setCustomer((Customer) jComboBoxCustomer.getSelectedItem());
            booking.setMovie((Movie) jComboBoxMovie.getSelectedItem());
            booking.setStudio((Studio) jComboBoxStudio.getSelectedItem());
            booking.setBookingDate(new java.sql.Date(jDateChooser1.getDate().getTime()));
            booking.setShowTime(java.sql.Time.valueOf(jComboBoxTime.getSelectedItem().toString()));
            booking.setSeatNumber(jLabelSeat.getText());
            Studio studio = (Studio) jComboBoxStudio.getSelectedItem();
            
            double total = studio.getTicketPrice();
            
            Customer customer = (Customer) jComboBoxCustomer.getSelectedItem();
            if (customer.getMembership().equalsIgnoreCase("Member")) {
                total *= 0.9;
            }
            booking.setTotalPrice(total);
            if (selectedId == -1) {
                bookingController.insert(booking);
                JOptionPane.showMessageDialog(this,
                        "Booking added successfully!");
            } else {
                booking.setBookingID(selectedId);
                bookingController.update(booking);
                JOptionPane.showMessageDialog(this,
                        "Booking updated successfully!");
            }
            loadTable();
            refreshSeat();
            clearForm();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage());
        }
    }//GEN-LAST:event_jButtonSaveActionPerformed

    private void jTableBookingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableBookingMouseClicked
        int row = jTableBooking.getSelectedRow();
        if (row == -1) {
            return;
        }
        Booking booking = bookingController.getAll().get(row);
        selectedId = booking.getBookingID();
        for (int i = 0; i < jComboBoxCustomer.getItemCount(); i++) {
            Customer customer = jComboBoxCustomer.getItemAt(i);

            if (customer.getCustomerID() == booking.getCustomer().getCustomerID()) {
                jComboBoxCustomer.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < jComboBoxMovie.getItemCount(); i++) {
            Movie movie = jComboBoxMovie.getItemAt(i);

            if (movie.getMovieID() == booking.getMovie().getMovieID()) {
                jComboBoxMovie.setSelectedIndex(i);
                break;
            }
        }
        for (int i = 0; i < jComboBoxStudio.getItemCount(); i++) {
            Studio studio = jComboBoxStudio.getItemAt(i);

            if (studio.getStudioID() == booking.getStudio().getStudioID()) {
                jComboBoxStudio.setSelectedIndex(i);
                break;
            }
        }
        jDateChooser1.setDate(booking.getBookingDate());
        jComboBoxTime.setSelectedItem(
                booking.getShowTime().toString());
        selectedSeat = booking.getSeatNumber();
        jLabelSeat.setText(selectedSeat);
        updateSummary();
    }//GEN-LAST:event_jTableBookingMouseClicked

    private void jButtonClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonClearActionPerformed
        clearForm();
    }//GEN-LAST:event_jButtonClearActionPerformed

    private void jButtonSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSearchActionPerformed
        searchData();
    }//GEN-LAST:event_jButtonSearchActionPerformed

    private void jTextFieldSearchKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextFieldSearchKeyReleased
        searchData();
    }//GEN-LAST:event_jTextFieldSearchKeyReleased

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed
        try{
            int totalData = bookingController.getTotalData();
            int totalPage = (int)Math.ceil((double)totalData / recordsPerPage);
            if(currentPage < totalPage){
                currentPage++;
                loadTable();

            }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jComboBoxStudioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxStudioActionPerformed
        Studio studio = (Studio) jComboBoxStudio.getSelectedItem();
        if (studio != null) {
        createSeat(studio);
        refreshSeat();
        updateSummary();
        }
    }//GEN-LAST:event_jComboBoxStudioActionPerformed

    private void jDateChooser1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jDateChooser1PropertyChange
        if ("date".equals(evt.getPropertyName())) {
            refreshSeat();
            updateSummary();
        }
    }//GEN-LAST:event_jDateChooser1PropertyChange

    private void jComboBoxTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxTimeActionPerformed
        refreshSeat();
        updateSummary();
    }//GEN-LAST:event_jComboBoxTimeActionPerformed

    private void jComboBoxMovieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxMovieActionPerformed
        updateSummary();
    }//GEN-LAST:event_jComboBoxMovieActionPerformed

    private void jComboBoxCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxCustomerActionPerformed
        updateSummary();
    }//GEN-LAST:event_jComboBoxCustomerActionPerformed

    private void jButtonBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonBackActionPerformed
            DashboardForm dashboard = new DashboardForm(user);
            dashboard.setVisible(true);
            dispose();
    }//GEN-LAST:event_jButtonBackActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new BookingForm().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonBack;
    private javax.swing.JButton jButtonClear;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JButton jButtonSave;
    private javax.swing.JButton jButtonSearch;
    private javax.swing.JComboBox<Customer> jComboBoxCustomer;
    private javax.swing.JComboBox<Movie> jComboBoxMovie;
    private javax.swing.JComboBox<Studio> jComboBoxStudio;
    private javax.swing.JComboBox<String> jComboBoxTime;
    private com.toedter.calendar.JDateChooser jDateChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelCustomer;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelMovie;
    private javax.swing.JLabel jLabelPrice;
    private javax.swing.JLabel jLabelSeat;
    private javax.swing.JLabel jLabelStudio;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JLabel jLabelTotal;
    private javax.swing.JPanel jPanelBackground;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelContent;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelInput;
    private javax.swing.JPanel jPanelScreen;
    private javax.swing.JPanel jPanelSeat;
    private javax.swing.JPanel jPanelSeatGrid;
    private javax.swing.JPanel jPanelSummary;
    private javax.swing.JPanel jPanelTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTableBooking;
    private javax.swing.JTextField jTextFieldSearch;
    // End of variables declaration//GEN-END:variables
}
