/**   Copyright 2013, Chris Henderson
***
*** Licensed under the Apache License, Version 2.0 (the "License");
*** you may not use this file except in compliance with the License.
*** You may obtain a copy of the License at
***
***     http://www.apache.org/licenses/LICENSE-2.0
***
*** Unless required by applicable law or agreed to in writing, software
*** distributed under the License is distributed on an "AS IS" BASIS,
*** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*** See the License for the specific language governing permissions and
*** limitations under the License.
***/

package csh.vctt.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import net.miginfocom.swing.MigLayout;
import csh.vctt.entities.ClassType;
import csh.vctt.entities.Player;
import csh.vctt.entities.Requirement;
import csh.vctt.entities.SkillWrapper;
import csh.vctt.entities.Tier;

/**
 * Contains all components of the graphical user interface used
 * to display the completion data to the user.
 * @author Chris
 *
 */
public class CertTreeGui extends JFrame{
	private static final long serialVersionUID = 698902060420563821L;

	private Dimension				m_screenDim;
	private List<Tier>				m_tiers;
	private Player					m_player;
	private List<String>			m_itemCerts;
	private SkillWrapper			m_skillCertWrapper;
	private JFrame					m_frameRef;
	private boolean					m_tierBoxTriggeredRefresh;
	
	private JComboBox<Tier>			m_tierBox;
	private JComboBox<ClassType>	m_classBox;
	private CertTreeTableModel		m_treeTableModel;
	private JLabel					m_totalLabel;
	private JLabel					m_completionLabel;
	private JLabel					m_balanceLabel;
	
	private static final String		M_CLASS_FILTER_ALL = "All";
	
	public CertTreeGui( String title, List<Tier> tiers, Player player, 
					    ArrayList<String> itemCerts, SkillWrapper skillCerts ){
		super( title );
		m_tiers = tiers;
		m_player = player;
		m_itemCerts = itemCerts;
		m_skillCertWrapper = skillCerts;
		m_frameRef = this;
		m_tierBoxTriggeredRefresh = false;
		
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		MigLayout migLay = new MigLayout();
		setLayout(migLay);
		
		m_screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		setSize( (int)( m_screenDim.width / 1.5 ), (int)( m_screenDim.height / 1.5 ) );
		int xWid = m_screenDim.width / 2 - getSize().width / 2;
		int yWid = m_screenDim.height / 2 - getSize().height / 2;
		setLocation( xWid, yWid );
	}
	
	/**
	 * Initializes the menu bar and filter drop-down menus
	 */
	public void initControls(){
		//Init Menu bar, menus, and items
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBorder( BorderFactory.createEtchedBorder( EtchedBorder.LOWERED ) );
		
		JMenu fileMenu = new JMenu( "File" );
		JMenuItem exitItem = new JMenuItem( "Exit" );
		exitItem.setToolTipText( "Exit VCTT" );
		exitItem.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ){
				dispatchEvent( new WindowEvent( m_frameRef, WindowEvent.WINDOW_CLOSING ) );
			}		
		});
		fileMenu.add( exitItem );
		menuBar.add( fileMenu );
		setJMenuBar( menuBar );
		
		//Init filter boxes and labels
		JPanel tierPanel = new JPanel();
		JLabel tierLabel = new JLabel( "Tier:" );
		tierPanel.add( tierLabel );
		
		m_tierBox = new JComboBox<Tier>();
		m_tierBox.setPreferredSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		m_tierBox.setMinimumSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		m_tierBox.setMaximumSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		fillTierBox();
		m_tierBox.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ){
				m_tierBoxTriggeredRefresh = true;
				fillClassBox();
				refreshTable();
				refreshInfoLabels();
				m_tierBoxTriggeredRefresh = false;
			}
		});
		tierPanel.add( m_tierBox );
		add( tierPanel, "gapright 160" );
		
		JPanel classPanel = new JPanel();
		JLabel classLabel = new JLabel( "Class:" );
		classPanel.add( classLabel );
		
		m_classBox = new JComboBox<ClassType>();
		m_classBox.setPreferredSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		m_classBox.setMinimumSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		m_classBox.setMaximumSize( new Dimension( m_screenDim.width / 4, m_screenDim.height / 35 ) );
		fillClassBox();
		m_classBox.addActionListener( new ActionListener(){
			@Override
			public void actionPerformed( ActionEvent e ){
				if( !m_tierBoxTriggeredRefresh ){
					refreshTable();
					refreshInfoLabels();
				}
				
			}	
		});
		classPanel.add( m_classBox );
		add( classPanel, "wrap" );
	}
	
	/**
	 * Initializes the completion table.
	 */
	public void initCertTreeTable(){
		List<String[]> tableDat = createTableDat( m_tiers.get( 0 ), M_CLASS_FILTER_ALL );
		m_treeTableModel = new CertTreeTableModel( tableDat );
		
		JTable treeTable = new JTable( m_treeTableModel );
		hideColumns( treeTable );
		TableColumn statCol = treeTable.getColumnModel().getColumn(CertTreeTableModel.COL_INDEX_STAT);
		statCol.setCellRenderer(new CertTreeTableRenderer());
		
		JScrollPane treeTablePane = new JScrollPane( treeTable );
		treeTablePane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
		treeTablePane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		treeTablePane.setVisible( true );
		treeTablePane.setPreferredSize( new Dimension( (int)(m_screenDim.width / 1.532), (int)( m_screenDim.height / 2.1 ) ) );
		treeTablePane.setMinimumSize( new Dimension( (int)(m_screenDim.width / 1.532), (int)( m_screenDim.height / 2.1 ) ) );
		treeTablePane.setMaximumSize( new Dimension( (int)(m_screenDim.width / 1.532), (int)( m_screenDim.height / 2.1 ) ) );
		
		add( treeTablePane, "span 2" );
	}
	
	/**
	 * Initializes the certification point information area.
	 */
	public void initInfoArea(){
		JPanel infoPanel = new JPanel();
		GridLayout gridLayout = new GridLayout( 3, 0 );
		infoPanel.setLayout(gridLayout );
		
		JPanel totalPanel = new JPanel();
		totalPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		JLabel totalHeading = new JLabel( "Total Cost of selection: " );
		m_totalLabel = new JLabel();
		totalPanel.add( totalHeading );
		totalPanel.add( m_totalLabel );
		infoPanel.add( totalPanel );
		
		JPanel compPanel = new JPanel();
		compPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		JLabel compHeading = new JLabel( "Additional certs needed to complete selection: ");
		m_completionLabel = new JLabel();
		compPanel.add( compHeading );
		compPanel.add( m_completionLabel );
		infoPanel.add( compPanel );
		
		JPanel balPanel = new JPanel();
		balPanel.setLayout( new FlowLayout( FlowLayout.LEFT ) );
		JLabel balHeading = new JLabel( "Current cert balance: " );
		m_balanceLabel = new JLabel( Integer.toString( m_player.getCertBal() ) );
		balPanel.add( balHeading );
		balPanel.add( m_balanceLabel );
		infoPanel.add( balPanel );
		
		refreshInfoLabels();
		
		add(Box.createRigidArea(new Dimension(0, 0)), "wrap");
		add(infoPanel);
	}
	
	/**
	 * Refreshes and re-displays the completion table based
	 * on the current user selection of the filter boxes.
	 */
	private void refreshTable(){
		Tier selectedTier = (Tier)m_tierBox.getSelectedItem();
		ClassType selectedClass = (ClassType)m_classBox.getSelectedItem();
		String classFilter = selectedClass.getName();
		List<String[]> tableDat = createTableDat( selectedTier, classFilter );
		m_treeTableModel.setDataVector( tableDat );
		
		validate();
		repaint();
	}
	
	/**
	 * Creates the data vector for the table model of the completion table.
	 * @param tier Currently selected tier.
	 * @param classFilter Currently selected class.
	 * @return Data vector representing the current table that should display.
	 */
	private List<String[]> createTableDat( Tier tier, String classFilter ){
		List<ClassType> classes = tier.getClasses();
		List<String[]> tabDat = new ArrayList<String[]>();
		
		for( ClassType curClass : classes ){
			if( classFilter.equals( M_CLASS_FILTER_ALL ) ||
					curClass.getName().equals(classFilter)){
				List<Requirement> reqs = curClass.getReqs();
			
				for( Requirement curReq : reqs ){
					String[] curRow = new String[7];
					curRow[0] = curReq.getName();
					curRow[1] = curReq.getCost();
					curRow[2] = ( curReq.getRequired().equals( "1" ) ) ? CertTreeTableModel.STAT_TEXT_INCOMPLETE : CertTreeTableModel.STAT_TEXT_OPTIONAL;
					curRow[3] = curReq.getId();
					curRow[4] = curReq.getRequired();
					curRow[5] = curReq.getItem();
					curRow[6] = curReq.getLineId();
				
					if( curRow[5].equals( "1" ) && m_itemCerts.contains( curRow[3] ) ){
						curRow[2] = CertTreeTableModel.STAT_TEXT_COMPLETE;
					}else if( curRow[3].equals( "-1" ) ){
						curRow[2] = CertTreeTableModel.STAT_TEXT_COMPLETE;
					}else if( curRow[5].equals( "0" ) ){
						boolean isComplete = false;
						if( curRow[6].equals( "-1" ) ){
							isComplete = m_skillCertWrapper.checkBasicCompletion( curRow[3] );
						}else{
							isComplete = m_skillCertWrapper.checkLineCompletion( curRow[0], curRow[6] );
						}
						
						if( isComplete ){
							curRow[2] = CertTreeTableModel.STAT_TEXT_COMPLETE;
						}
					}
				
					tabDat.add( curRow );
				}
			}
		}
		
		return tabDat;
	}
	
	private void fillTierBox(){
		m_tierBox.removeAllItems();
		for( Tier curTier : m_tiers ){
			m_tierBox.addItem( curTier );
		}
	}
	
	private void fillClassBox(){
		m_classBox.removeAllItems();
		m_classBox.addItem(new ClassType( M_CLASS_FILTER_ALL ) );
		Tier selectedTier = (Tier)m_tierBox.getSelectedItem();
		List<ClassType> classes = selectedTier.getClasses();
		for( ClassType curClass : classes ){
			m_classBox.addItem( curClass );
		}
	}
	
	/**
	 * Update the certification balance info area
	 * based on the current user filter selction.
	 */
	private void refreshInfoLabels(){
		int totalCost = 0;
		for( int i = 0; i < m_treeTableModel.getRowCount(); i++ ){
			String status = (String)m_treeTableModel.getValueAt(i, CertTreeTableModel.COL_INDEX_STAT);
			
			if(status.equals(CertTreeTableModel.STAT_TEXT_INCOMPLETE)){
				String certCost = (String)m_treeTableModel.getValueAt( i, CertTreeTableModel.COL_INDEX_COST );
				totalCost += Integer.parseInt( certCost );
			}
		}
		
		m_totalLabel.setText( Integer.toString( totalCost ) );
		
		if( totalCost - m_player.getCertBal() > 0){
			m_completionLabel.setText( Integer.toString( totalCost - m_player.getCertBal() ) );
		}else{
			m_completionLabel.setText( "0" );
		}
	}
	
	/**
	 * Hide the data columns that need not be displayed.
	 * @param table
	 */
	private void hideColumns( JTable table ){
		TableColumnModel colModel = table.getColumnModel();
		TableColumn lineIdCol	= colModel.getColumn( CertTreeTableModel.COL_INDEX_LINEID );
		TableColumn isItemCol 	= colModel.getColumn( CertTreeTableModel.COL_INDEX_ISITEM	);
		TableColumn isReqdCol 	= colModel.getColumn( CertTreeTableModel.COL_INDEX_ISREQD );
		TableColumn idCol 		= colModel.getColumn( CertTreeTableModel.COL_INDEX_ID );
		table.removeColumn( lineIdCol );
		table.removeColumn( isItemCol );
		table.removeColumn( isReqdCol );
		table.removeColumn( idCol );
		table.validate();
	}
	
	/**
	 * CertTreeTableModel inner class lays out the table model structure for the
	 * completion table.
	 * @author Chris
	 * 
	 */
	private static class CertTreeTableModel extends AbstractTableModel{
		private static final long 	serialVersionUID = -4228096388870572652L;
		
		private static final String		STAT_TEXT_COMPLETE = "Complete";
		private static final String		STAT_TEXT_INCOMPLETE = "Incomplete";
		private static final String		STAT_TEXT_OPTIONAL = "Optional";
		
		private static final int 		COL_INDEX_COST = 1;
		private static final int		COL_INDEX_STAT = 2;
		private static final int		COL_INDEX_ID = 3;
		private static final int		COL_INDEX_ISREQD = 4;
		private static final int		COL_INDEX_ISITEM = 5;
		private static final int		COL_INDEX_LINEID = 6;
		
		private String[] 				columnNames;
		private List<String[]> 			reqs;
		
		public CertTreeTableModel( List<String[]> reqPar ){
			columnNames = new String[]{ "Certification", "Cost", "Status", "hidId", "hidIsReqd", "hidIsItem", "hidLineId" };
			reqs = reqPar;
		}
		
		@Override
		public String getColumnName( int column ){ return columnNames[column]; }
		
		@Override
		public int getRowCount(){ return reqs.size(); }

		@Override
		public int getColumnCount(){ return columnNames.length; }

		@Override
		public Object getValueAt( int rowIndex, int columnIndex ){ return reqs.get( rowIndex )[columnIndex]; }
		
		public void setDataVector( List<String[]> reqsPar ){ 
			reqs = reqsPar;
			fireTableDataChanged();
		}
	}
	
	/**
	 * CertTreeTableRenderer inner class lays out the custom renderer used
	 * to color the status column of the completion table.
	 * @author Chris
	 *
	 */
	private static class CertTreeTableRenderer extends DefaultTableCellRenderer{
		private static final long serialVersionUID = -6332907027712302601L;

		public Component getTableCellRendererComponent ( JTable table, Object obj, boolean isSelected, 
													     boolean hasFocus, int row, int column ){
			Component cell = super.getTableCellRendererComponent( table, obj, isSelected, hasFocus, row, column );
			
			if( column == CertTreeTableModel.COL_INDEX_STAT ){
				String value = ( String )table.getValueAt( row, column );
				if( value.equals( CertTreeTableModel.STAT_TEXT_COMPLETE ) ){
					cell.setBackground( Color.GREEN ) ;
				}else if( value.equals( CertTreeTableModel.STAT_TEXT_INCOMPLETE ) ){
					cell.setBackground( Color.RED );
				}else if( value.equals( CertTreeTableModel.STAT_TEXT_OPTIONAL ) ){
					cell.setBackground( Color.LIGHT_GRAY );
				}
			}
			
			return cell;
		}
	}
}
