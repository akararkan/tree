import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EnhancedCourseScheduler extends JFrame {
    private JTable scheduleTable;
    private JList<String> schedulesList;
    private DefaultListModel<String> schedulesListModel;
    private JLabel creditsLabel;
    private JLabel daysLabel;
    private JLabel conflictsLabel;
    private JTextArea timeRestArea;
    private JTextArea detailedScheduleArea;
    private List<List<Course>> allPossibleSchedules;
    private int currentScheduleIndex = 0;

    public EnhancedCourseScheduler(List<Course> courses) {
        super("Enhanced Course Scheduler");
        setSize(1300, 900);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Main panel with split layout
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.3); // Adjust the split ratio

        // Left panel for schedules list
        JPanel leftPanel = createLeftPanel();
        schedulesListModel = new DefaultListModel<>();
        schedulesList = new JList<>(schedulesListModel);
        schedulesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane schedulesScroll = new JScrollPane(schedulesList);
        schedulesList.setToolTipText("Select a schedule to view details");
        leftPanel.add(new JLabel("Possible Schedules", SwingConstants.CENTER), BorderLayout.NORTH);
        leftPanel.add(schedulesScroll, BorderLayout.CENTER);

        // Right panel for schedule details
        JPanel rightPanel = createRightPanel();

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);

        // Enhanced Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton generateButton = new JButton("Generate Possible Schedules");
        JButton clearButton = new JButton("Clear Schedules");
        JButton exportButton = new JButton("Export to PDF");
        JButton chooseButton = new JButton("Choose Best Schedule");

        generateButton.addActionListener(e -> {
            showLoadingAnimation(() -> {
                generatePossibleSchedules(courses);
                if (!allPossibleSchedules.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        updateSchedulesList();
                        schedulesList.setSelectedIndex(0);
                        displaySchedule(0);
                    });
                }
            });
        });
        clearButton.addActionListener(e -> clearSchedules());
        exportButton.addActionListener(e -> exportToPDF());
        chooseButton.addActionListener(e -> chooseBestSchedule());

        controlPanel.add(generateButton);
        controlPanel.add(clearButton);
        controlPanel.add(exportButton);
        controlPanel.add(chooseButton);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.setBackground(Color.decode("#e8eaf6"));
        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Table for current schedule
        String[] columnNames = {"Course", "Days", "Time", "Credits"};
        scheduleTable = new JTable(new DefaultTableModel(columnNames, 0));
        scheduleTable.setFillsViewportHeight(true);
        scheduleTable.setRowHeight(30);
        JScrollPane tableScroll = new JScrollPane(scheduleTable);

        // Info panel
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        creditsLabel = new JLabel("Total Credits: 0");
        daysLabel = new JLabel("Days of Week: 0");
        conflictsLabel = new JLabel("Conflicts: None");
        timeRestArea = new JTextArea(3, 40);
        timeRestArea.setEditable(false);
        timeRestArea.setBorder(BorderFactory.createTitledBorder("Time Between Classes"));

        detailedScheduleArea = new JTextArea(5, 40);
        detailedScheduleArea.setEditable(false);
        detailedScheduleArea.setBorder(BorderFactory.createTitledBorder("Detailed Schedule Information"));

        infoPanel.add(creditsLabel);
        infoPanel.add(daysLabel);
        infoPanel.add(conflictsLabel);
        infoPanel.add(new JScrollPane(timeRestArea));
        infoPanel.add(new JScrollPane(detailedScheduleArea));

        // Navigation panel with styled buttons
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        JButton prevButton = new JButton("< Previous");
        JButton nextButton = new JButton("Next >");
        JButton resetButton = new JButton("Reset");

        // Tooltips for buttons
        prevButton.setToolTipText("View previous schedule");
        nextButton.setToolTipText("View next schedule");
        resetButton.setToolTipText("Reset to the first schedule");

        // Adding buttons to navigation panel
        navPanel.add(prevButton);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacer
        navPanel.add(nextButton);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Spacer
        navPanel.add(resetButton);

        // Event Listeners
        schedulesList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = schedulesList.getSelectedIndex();
                if (index >= 0) {
                    currentScheduleIndex = index;
                    displaySchedule(index);
                }
            }
        });

        prevButton.addActionListener(e -> {
            if (currentScheduleIndex > 0) {
                currentScheduleIndex--;
                displaySchedule(currentScheduleIndex);
                schedulesList.setSelectedIndex(currentScheduleIndex);
            }
        });

        nextButton.addActionListener(e -> {
            if (currentScheduleIndex < allPossibleSchedules.size() - 1) {
                currentScheduleIndex++;
                displaySchedule(currentScheduleIndex);
                schedulesList.setSelectedIndex(currentScheduleIndex);
            }
        });

        resetButton.addActionListener(e -> {
            currentScheduleIndex = 0;
            schedulesList.setSelectedIndex(currentScheduleIndex);
            displaySchedule(currentScheduleIndex);
        });

        rightPanel.add(tableScroll, BorderLayout.CENTER);
        rightPanel.add(infoPanel, BorderLayout.SOUTH);
        rightPanel.add(navPanel, BorderLayout.NORTH);
        return rightPanel;
    }

    private void showLoadingAnimation(Runnable task) {
        JDialog loadingDialog = new JDialog(this, "Loading", true);
        loadingDialog.setSize(300, 150);
        loadingDialog.setLayout(new BorderLayout());
        loadingDialog.setLocationRelativeTo(this);

        JLabel loadingLabel = new JLabel("Generating schedules... Please wait.", SwingConstants.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        loadingDialog.add(loadingLabel, BorderLayout.NORTH);
        loadingDialog.add(progressBar, BorderLayout.CENTER);
        loadingDialog.setUndecorated(true);

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                task.run();
                return null;
            }

            @Override
            protected void done() {
                loadingDialog.dispose();
            }
        };

        worker.execute();
        loadingDialog.setVisible(true);
    }

    private void generatePossibleSchedules(List<Course> courses) {
        allPossibleSchedules = new ArrayList<>();
        List<Course> currentSet = new ArrayList<>();
        int maxCredit = 18;
        int currentCredit = 0;

        for (Course course : courses) {
            int potentialCredit = currentCredit + course.getCredit();

            if (!Main.hasTimeConflict(currentSet, course) && potentialCredit <= maxCredit) {
                currentSet.add(course);
                currentCredit += course.getCredit();
            } else {
                if (!currentSet.isEmpty() && calculateTotalCredits(currentSet) >= 10) {
                    allPossibleSchedules.add(new ArrayList<>(currentSet));
                }
                currentSet.clear();
                currentSet.add(course);
                currentCredit = course.getCredit();
            }

            // Show permutations for each course
            List<List<Course>> tempLists = new ArrayList<>();
            Main.showPermutations(tempLists, new ArrayList<>(currentSet), course);
            for (List<Course> list : tempLists) {
                if (calculateTotalCredits(list) >= 10 && calculateTotalCredits(list) <= maxCredit) {
                    allPossibleSchedules.add(list);
                }
            }
        }
    }

    private void updateSchedulesList() {
        schedulesListModel.clear();
        for (int i = 0; i < allPossibleSchedules.size(); i++) {
            List<Course> schedule = allPossibleSchedules.get(i);
            schedulesListModel.addElement(String.format("Schedule %d (%d credits)",
                    i + 1, calculateTotalCredits(schedule)));
        }
    }

    private void displaySchedule(int index) {
        if (index < 0 || index >= allPossibleSchedules.size()) return;

        List<Course> schedule = allPossibleSchedules.get(index);

        // Update table
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0);
        for (Course course : schedule) {
            model.addRow(new Object[]{
                    course.getName(),
                    String.join(", ", course.getDays()),
                    course.getDayAndTime(),
                    course.getCredit()
            });
        }

        // Update info labels
        creditsLabel.setText("Total Credits: " + calculateTotalCredits(schedule));
        daysLabel.setText("Days of Week: " + Main.numOfDays(schedule));

        List<List<Course>> conflicts = Main.findTimeConflicts(schedule);
        conflictsLabel.setText("Conflicts: " + (conflicts.isEmpty() ? "None" : conflicts.size() + " found"));

        // Update time rest area
        timeRestArea.setText("Time Between Classes:\n" + Main.calculateTimeRest(schedule));

        // Update detailed schedule area
        StringBuilder detailedInfo = new StringBuilder();
        for (Course course : schedule) {
            detailedInfo.append(String.format("Course: %s\nCredits: %d\nDays: %s\nTime: %s\n\n",
                    course.getName(),  course.getCredit(), String.join(", ", course.getDays()), course.getDayAndTime()));
        }
        detailedScheduleArea.setText(detailedInfo.toString());
    }

    private void clearSchedules() {
        schedulesListModel.clear();
        DefaultTableModel model = (DefaultTableModel) scheduleTable.getModel();
        model.setRowCount(0);
        creditsLabel.setText("Total Credits: 0");
        daysLabel.setText("Days of Week: 0");
        conflictsLabel.setText("Conflicts: None");
        timeRestArea.setText("");
        detailedScheduleArea.setText("");
        if (allPossibleSchedules != null) {
            allPossibleSchedules.clear();
        }
    }

    private void exportToPDF() {
        // Placeholder for PDF export functionality
        JOptionPane.showMessageDialog(this, "Exporting to PDF... (Feature coming soon!)", "Export", JOptionPane.INFORMATION_MESSAGE);
    }

    private int calculateTotalCredits(List<Course> courses) {
        return courses.stream().mapToInt(Course::getCredit).sum();
    }

    private void chooseBestSchedule() {
        if (allPossibleSchedules == null || allPossibleSchedules.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No schedules available to choose from!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Course> bestSchedule = allPossibleSchedules.stream()
                .max(Comparator.comparingInt(this::calculateTotalCredits)
                        .thenComparingInt(list -> -Main.numOfDays(list))
                        .thenComparingInt(this::calculateTimeRestValue)
                        .thenComparingInt(this::calculateUnlockDepth))
                .orElse(allPossibleSchedules.get(0));

        currentScheduleIndex = allPossibleSchedules.indexOf(bestSchedule);
        schedulesList.setSelectedIndex(currentScheduleIndex);
        displaySchedule(currentScheduleIndex);
    }

    private int calculateTimeRestValue(List<Course> courses) {
        return courses.stream()
                .flatMap(firstCourse -> courses.stream()
                        .filter(secondCourse -> !firstCourse.equals(secondCourse))
                        .flatMap(secondCourse ->
                                Arrays.stream(firstCourse.getDays())
                                        .flatMap(day1 ->
                                                Arrays.stream(secondCourse.getDays())
                                                        .filter(day1::equals)
                                                        .map(day -> {
                                                            if (firstCourse.getDayAndTime().contains(day) && secondCourse.getDayAndTime().contains(day)) {
                                                                return Math.abs((int) Duration.between(firstCourse.getEndTime(), secondCourse.getStartTime()).toMinutes());
                                                            } else {
                                                                return 0;
                                                            }
                                                        })
                                                        .filter(timeRest -> timeRest > 0)
                                        )
                        )
                )
                .mapToInt(Integer::intValue)
                .sum();
    }

    private int calculateUnlockDepth(List<Course> courses) {
        return courses.stream()
                .mapToInt(course -> calculateDepth(course, new HashSet<>()))
                .sum();
    }

    private int calculateDepth(Course course, Set<Course> visited) {
        if (visited.contains(course) || course.getUnlocks().isEmpty()) {
            return 0;
        }
        visited.add(course);
        return 1 + course.getUnlocks().stream().mapToInt(unlock -> calculateDepth(course, visited)).max().orElse(0);
    }

    public static void main(String[] args) {
        List<Course> courseList = JsonLoader.loadCourses("/home/akar/Music/tree/src/main/java/courses.json");
        SwingUtilities.invokeLater(() -> {
            EnhancedCourseScheduler scheduler = new EnhancedCourseScheduler(courseList);
            scheduler.setVisible(true);
        });
    }
}
