package clientRMI;


import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;



@SuppressWarnings("serial")
public class GenericTable extends JTable {

	private DefaultTableModel tableModel = new DefaultTableModel();

	public GenericTable(Object[] columns)
	{
		super();
		initiateTable(columns);
	}


	private void initiateTable(Object[] columns)
	{
		setModel(tableModel);
		setShowHorizontalLines(true);
		setShowVerticalLines(true);
		setShowGrid(true);
		getTableHeader().setReorderingAllowed(false);


		for(Object column : columns)
		{
			tableModel.addColumn(column);
		}

	}

	public void add(Object[] rowItem)
	{
		tableModel.addRow(rowItem);
	}

	public void remove(int index)
	{
		tableModel.removeRow(index);
	}

	public int getSelectedItem()
	{
		return getSelectedRow();
	}

	public void clear()
	{
		for (int i = getRowCount()-1; i >= 0; i--) {
			remove(i);
		}
	}

	public boolean isCellEditable(int rowIndex, int vColIndex) {
		return false;
	}
}
