import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Iterator;

public class Window extends JFrame {
    private JPanel mainPanel;
    private JTable inputTable;
    private JTable iterateByKeyTable;
    private JTable iterateByPutOrderNumberTable;
    private JButton iterateMapBut;
    private JButton addColBut;
    private JButton removeColBut;

    Window() {

        this.setContentPane(mainPanel);
        this.setName("Map Test");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setExtendedState(MAXIMIZED_BOTH);

        DefaultTableModel inputModel = new DefaultTableModel();
        inputTable.setModel(inputModel);
        inputModel.setDataVector(new Object[2][10], getHeaders(10));
        DefaultTableModel dataIterateByPutOrderNumber = new DefaultTableModel();
        iterateByPutOrderNumberTable.setModel(dataIterateByPutOrderNumber);
        dataIterateByPutOrderNumber.setDataVector(new Object[2][10], getHeaders(10));
        DefaultTableModel dataIterateByKeyModel = new DefaultTableModel();
        iterateByKeyTable.setModel(dataIterateByKeyModel);
        dataIterateByKeyModel.setDataVector(new Object[2][10], getHeaders(10));

        removeColBut.addActionListener(actionEvent -> {
            Object[][] data = getTableData(inputModel);
            String[] headers = getHeaders(inputModel.getColumnCount() - 1);
            inputModel.setDataVector(data, headers);
        });

        addColBut.addActionListener(actionEvent -> {
            Object[][] data = getTableData(inputModel);
            String[] headers = getHeaders(inputModel.getColumnCount() + 1);
            inputModel.setDataVector(data, headers);
        });

        iterateMapBut.addActionListener(actionEvent -> {
            String[][] data = getStringsFromTable(inputModel);
            PutOrderMap<Integer, String> map = new PutOrderMap<>();
            try {
                for (int i = 0; i < data[0].length; i++) {
                    map.add(Integer.parseInt(data[0][i]), data[1][i]);
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(null,
                        "Upper row must have numbers",
                        "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[][] iteratedByPutOrderNumberData = new String[2][map.size()];
            Iterator<Integer> keyIteratorByPutOrderNumber = map.keyIteratorByPutOrderNumber();

            int i = 0;
            while (keyIteratorByPutOrderNumber.hasNext()) {
                iteratedByPutOrderNumberData[0][i] = String.valueOf(keyIteratorByPutOrderNumber.next());
                i++;
            }

            i = 0;
            for (String value : map) {
                iteratedByPutOrderNumberData[1][i] = value;
                i++;
            }

            dataIterateByPutOrderNumber.setDataVector(iteratedByPutOrderNumberData,
                    getHeaders(iteratedByPutOrderNumberData[0].length));

            String[][] iteratedByKeyData = new String[2][map.size()];

            Iterator<Integer> keyIterator = map.keyIterator();
            i = 0;
            while (keyIterator.hasNext()) {
                iteratedByKeyData[0][i] = String.valueOf(keyIterator.next());
                i++;
            }

            Iterator<String> iteratorByKeys = map.iteratorByKeys();
            i = 0;
            while (iteratorByKeys.hasNext()) {
                iteratedByKeyData[1][i] = iteratorByKeys.next();
                i++;
            }

            dataIterateByKeyModel.setDataVector(iteratedByKeyData,
                    getHeaders(iteratedByKeyData[0].length));
        });
    }

    private String[][] getStringsFromTable(DefaultTableModel model) {
        String[][] data = new String[model.getRowCount()][model.getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++)
            for (int j = 0; j < model.getColumnCount(); j++)
                data[i][j] = (String) model.getValueAt(i, j);
        return data;
    }

    private String[] getHeaders(int length) {
        String[] headers = new String[length];
        for (int i = 0; i < length; i++) headers[i] = Integer.toString(i + 1);
        return headers;
    }

    private Object[][] getTableData(DefaultTableModel model) {
        Object[][] data = new Object[model.getRowCount()][model.getColumnCount()];
        for (int i = 0; i < model.getRowCount(); i++)
            for (int j = 0; j < model.getColumnCount(); j++) data[i][j] = model.getValueAt(i, j);
        return data;
    }
}
