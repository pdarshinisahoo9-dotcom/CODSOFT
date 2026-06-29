import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

// ================================================
//  Student - data model
// ================================================
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    private String rollNumber, name, grade, email, phone, course;

    public Student(String rollNumber, String name, String grade,
                   String email, String phone, String course) {
        this.rollNumber = rollNumber.trim();
        this.name       = name.trim();
        this.grade      = grade.trim();
        this.email      = email.trim();
        this.phone      = phone.trim();
        this.course     = course.trim();
    }

    public String getRollNumber() { return rollNumber; }
    public String getName()       { return name; }
    public String getGrade()      { return grade; }
    public String getEmail()      { return email; }
    public String getPhone()      { return phone; }
    public String getCourse()     { return course; }

    public void setName(String v)   { name   = v.trim(); }
    public void setGrade(String v)  { grade  = v.trim(); }
    public void setEmail(String v)  { email  = v.trim(); }
    public void setPhone(String v)  { phone  = v.trim(); }
    public void setCourse(String v) { course = v.trim(); }

    public String[] toRow() {
        return new String[]{ rollNumber, name, grade, course, email, phone };
    }

    public String toCsv() {
        return rollNumber + "," + name + "," + grade + ","
             + email + "," + phone + "," + course;
    }

    public static Student fromCsv(String line) {
        String[] p = line.split(",", 6);
        if (p.length < 6) return null;
        return new Student(p[0], p[1], p[2], p[3], p[4], p[5]);
    }
}

// ================================================
//  StudentManagementSystem - core logic + file I/O
// ================================================
class StudentManagementSystem {
    private final Map<String, Student> students = new LinkedHashMap<>();
    private final String dataFile;

    public StudentManagementSystem(String dataFile) {
        this.dataFile = dataFile;
        loadFromFile();
    }

    public String addStudent(Student s) {
        if (students.containsKey(s.getRollNumber()))
            return "ERROR: Roll number " + s.getRollNumber() + " already exists.";
        students.put(s.getRollNumber(), s);
        saveToFile();
        return "OK";
    }

    public String removeStudent(String roll) {
        if (!students.containsKey(roll))
            return "ERROR: Student with roll number " + roll + " not found.";
        students.remove(roll);
        saveToFile();
        return "OK";
    }

    public Student searchStudent(String roll) {
        return students.get(roll);
    }

    public List<Student> searchByName(String name) {
        List<Student> result = new ArrayList<>();
        for (Student s : students.values())
            if (s.getName().toLowerCase().contains(name.toLowerCase()))
                result.add(s);
        return result;
    }

    public String updateStudent(String roll, String name, String grade,
                                String email, String phone, String course) {
        Student s = students.get(roll);
        if (s == null) return "ERROR: Student not found.";
        s.setName(name); s.setGrade(grade); s.setEmail(email);
        s.setPhone(phone); s.setCourse(course);
        saveToFile();
        return "OK";
    }

    public Collection<Student> getAllStudents() { return students.values(); }
    public int getTotalStudents()               { return students.size(); }

    private void saveToFile() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(dataFile))) {
            for (Student s : students.values()) pw.println(s.toCsv());
        } catch (IOException e) { System.err.println("Save error: " + e.getMessage()); }
    }

    private void loadFromFile() {
        File f = new File(dataFile);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    Student s = Student.fromCsv(line);
                    if (s != null) students.put(s.getRollNumber(), s);
                }
            }
        } catch (IOException e) { System.err.println("Load error: " + e.getMessage()); }
    }
}

// ================================================
//  StudentManagement - Swing GUI
// ================================================
public class StudentManagement extends JFrame {

    private static final Color C_BG      = new Color(245, 247, 250);
    private static final Color C_PRIMARY = new Color(41,  128, 185);
    private static final Color C_ACCENT  = new Color(39,  174,  96);
    private static final Color C_DANGER  = new Color(192,  57,  43);
    private static final Color C_HEADER  = new Color(52,   73,  94);
    private static final Color C_WHITE   = Color.WHITE;

    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font FONT_FIELD = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_BTN   = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font FONT_TABLE = new Font("Segoe UI", Font.PLAIN, 13);

    private final StudentManagementSystem sms = new StudentManagementSystem("students.csv");

    private DefaultTableModel tableModel;
    private JTable  table;
    private JLabel  statusLabel, countLabel;
    private JTextField searchField;
    private JTextField fRoll, fName, fGrade, fEmail, fPhone, fCourse;

    public StudentManagement() {
        buildUI();
        refreshTable();
        setVisible(true);
    }

    private void buildUI() {
        setTitle("Student Management System");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(C_BG);
        add(buildHeader(),  BorderLayout.NORTH);
        add(buildCenter(),  BorderLayout.CENTER);
        add(buildStatus(),  BorderLayout.SOUTH);
    }

    // ---- Header ----
    private JPanel buildHeader() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel title = new JLabel("  Student Management System");
        title.setFont(FONT_TITLE);
        title.setForeground(C_WHITE);

        countLabel = new JLabel();
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        countLabel.setForeground(new Color(189, 195, 199));

        p.add(title,      BorderLayout.WEST);
        p.add(countLabel, BorderLayout.EAST);
        return p;
    }

    // ---- Centre split pane ----
    private JSplitPane buildCenter() {
        JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, buildForm(), buildTablePanel());
        sp.setDividerLocation(300);
        sp.setDividerSize(5);
        sp.setBorder(null);
        return sp;
    }

    // ---- Left form panel ----
    private JPanel buildForm() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setBackground(C_WHITE);
        outer.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(220, 220, 220)));

        JLabel head = new JLabel("  Student Details");
        head.setFont(new Font("Segoe UI", Font.BOLD, 14));
        head.setForeground(C_PRIMARY);
        head.setBorder(new EmptyBorder(12, 12, 8, 12));
        outer.add(head, BorderLayout.NORTH);

        fRoll = field(); fName = field(); fGrade = field();
        fEmail = field(); fPhone = field(); fCourse = field();

        String[] labels = {"Roll Number *", "Full Name *", "Grade *", "Course", "Email", "Phone"};
        JTextField[] fields = {fRoll, fName, fGrade, fCourse, fEmail, fPhone};

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(C_WHITE);
        form.setBorder(new EmptyBorder(4, 14, 4, 14));
        GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1;
        gc.insets = new Insets(4, 2, 2, 2);

        for (int i = 0; i < labels.length; i++) {
            gc.gridy = i * 2;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(FONT_LABEL);
            lbl.setForeground(C_HEADER);
            form.add(lbl, gc);
            gc.gridy = i * 2 + 1;
            form.add(fields[i], gc);
        }

        outer.add(new JScrollPane(form,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        outer.add(buildFormButtons(), BorderLayout.SOUTH);
        return outer;
    }

    private JPanel buildFormButtons() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBackground(C_WHITE);
        p.setBorder(new EmptyBorder(8, 14, 14, 14));

        JButton bAdd    = btn("Add Student", C_ACCENT,  C_WHITE);
        JButton bUpdate = btn("Update",      C_PRIMARY, C_WHITE);
        JButton bDelete = btn("Delete",      C_DANGER,  C_WHITE);
        JButton bClear  = btn("Clear",       C_HEADER,  C_WHITE);

        bAdd.addActionListener(e -> addStudent());
        bUpdate.addActionListener(e -> updateStudent());
        bDelete.addActionListener(e -> deleteStudent());
        bClear.addActionListener(e -> clearForm());

        p.add(bAdd); p.add(bUpdate); p.add(bDelete); p.add(bClear);
        return p;
    }

    // ---- Right table panel ----
    private JPanel buildTablePanel() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(C_BG);
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        p.add(buildSearchBar(), BorderLayout.NORTH);
        p.add(buildTable(),     BorderLayout.CENTER);
        return p;
    }

    private JPanel buildSearchBar() {
        JPanel p = new JPanel(new BorderLayout(6, 0));
        p.setBackground(C_BG);

        searchField = field();
        searchField.setToolTipText("Search by name or roll number");

        JButton bSearch = btn("Search",   C_PRIMARY, C_WHITE);
        JButton bAll    = btn("Show All", C_HEADER,  C_WHITE);

        bSearch.addActionListener(e -> searchStudents());
        bAll.addActionListener(e    -> refreshTable());
        searchField.addActionListener(e -> searchStudents());

        JLabel lbl = new JLabel("  Search: ");
        lbl.setFont(FONT_LABEL);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        right.setBackground(C_BG);
        right.add(bSearch); right.add(bAll);

        p.add(lbl,         BorderLayout.WEST);
        p.add(searchField, BorderLayout.CENTER);
        p.add(right,       BorderLayout.EAST);
        return p;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Roll No", "Name", "Grade", "Course", "Email", "Phone"};
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_TABLE);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(214, 234, 248));
        table.setSelectionForeground(C_HEADER);
        table.setBackground(C_WHITE);

        JTableHeader h = table.getTableHeader();
        h.setFont(new Font("Segoe UI", Font.BOLD, 13));
        h.setBackground(C_PRIMARY);
        h.setForeground(C_WHITE);
        h.setPreferredSize(new Dimension(0, 34));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, v, sel, foc, row, col);
                if (!sel) setBackground(row % 2 == 0 ? C_WHITE : new Color(240, 244, 250));
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return this;
            }
        });

        int[] widths = {80, 150, 55, 140, 175, 110};
        for (int i = 0; i < widths.length; i++)
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromTable();
        });

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createLineBorder(new Color(200, 210, 220)));
        return sp;
    }

    // ---- Status bar ----
    private JPanel buildStatus() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(C_HEADER);
        p.setBorder(new EmptyBorder(5, 16, 5, 16));
        statusLabel = new JLabel("Ready  |  Data saved to students.csv");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(189, 195, 199));
        p.add(statusLabel, BorderLayout.WEST);
        return p;
    }

    // ---- Actions ----
    private void addStudent() {
        if (!validateForm()) return;
        Student s = new Student(fRoll.getText(), fName.getText(), fGrade.getText(),
                                fEmail.getText(), fPhone.getText(), fCourse.getText());
        String r = sms.addStudent(s);
        if (r.startsWith("ERROR")) { showError(r.substring(7)); return; }
        showStatus("Student added: " + s.getName());
        clearForm(); refreshTable();
    }

    private void updateStudent() {
        if (fRoll.getText().trim().isEmpty()) {
            showError("Select a student from the table to update."); return;
        }
        if (!validateForm()) return;
        String r = sms.updateStudent(fRoll.getText(), fName.getText(), fGrade.getText(),
                                     fEmail.getText(), fPhone.getText(), fCourse.getText());
        if (r.startsWith("ERROR")) { showError(r.substring(7)); return; }
        showStatus("Student updated successfully.");
        clearForm(); refreshTable();
    }

    private void deleteStudent() {
        String roll = fRoll.getText().trim();
        if (roll.isEmpty()) { showError("Select a student to delete."); return; }
        int ok = JOptionPane.showConfirmDialog(this,
            "Delete student with Roll No: " + roll + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (ok != JOptionPane.YES_OPTION) return;
        String r = sms.removeStudent(roll);
        if (r.startsWith("ERROR")) { showError(r.substring(7)); return; }
        showStatus("Student deleted.");
        clearForm(); refreshTable();
    }

    private void searchStudents() {
        String q = searchField.getText().trim();
        if (q.isEmpty()) { refreshTable(); return; }
        tableModel.setRowCount(0);
        Student byRoll = sms.searchStudent(q);
        if (byRoll != null) {
            tableModel.addRow(byRoll.toRow());
        } else {
            for (Student s : sms.searchByName(q)) tableModel.addRow(s.toRow());
        }
        showStatus("Results for '" + q + "': " + tableModel.getRowCount() + " found");
        updateCount();
    }

    // ---- Helpers ----
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (Student s : sms.getAllStudents()) tableModel.addRow(s.toRow());
        showStatus("Ready  |  " + sms.getTotalStudents() + " student(s) in database.");
        updateCount();
    }

    private void fillFormFromTable() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        Student s = sms.searchStudent((String) tableModel.getValueAt(row, 0));
        if (s == null) return;
        fRoll.setText(s.getRollNumber()); fRoll.setEditable(false);
        fName.setText(s.getName());     fGrade.setText(s.getGrade());
        fEmail.setText(s.getEmail());   fPhone.setText(s.getPhone());
        fCourse.setText(s.getCourse());
    }

    private boolean validateForm() {
        if (fRoll.getText().trim().isEmpty())  { showError("Roll Number is required."); return false; }
        if (fName.getText().trim().isEmpty())  { showError("Name is required."); return false; }
        if (fGrade.getText().trim().isEmpty()) { showError("Grade is required."); return false; }
        String email = fEmail.getText().trim();
        if (!email.isEmpty() && !email.matches("^[\\w.+\\-]+@[\\w\\-]+\\.[a-zA-Z]{2,}$")) {
            showError("Enter a valid email address."); return false;
        }
        String phone = fPhone.getText().trim();
        if (!phone.isEmpty() && !phone.matches("\\d{7,15}")) {
            showError("Phone must be 7-15 digits."); return false;
        }
        return true;
    }

    private void clearForm() {
        fRoll.setText(""); fRoll.setEditable(true);
        fName.setText(""); fGrade.setText(""); fEmail.setText("");
        fPhone.setText(""); fCourse.setText("");
        table.clearSelection();
    }

    private void showStatus(String m) { statusLabel.setText(m); }
    private void showError(String m) {
        JOptionPane.showMessageDialog(this, m, "Error", JOptionPane.ERROR_MESSAGE);
    }
    private void updateCount() {
        countLabel.setText("Total: " + tableModel.getRowCount() + "   ");
    }

    private JTextField field() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_FIELD);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            new EmptyBorder(5, 8, 5, 8)));
        return tf;
    }

    private JButton btn(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BTN);
        b.setBackground(bg); b.setForeground(fg);
        b.setFocusPainted(false); b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(115, 32));
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(bg); }
        });
        return b;
    }

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(StudentManagement::new);
    }
}
