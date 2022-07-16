
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class Window extends JFrame implements ActionListener {

    Service service;

    public JTextField jTextFieldCountry;
    public JTextField jTextFieldCity;
    public JTextField jTextFieldCurrency;

    public Window(){

        JPanel jPanel = new JPanel();
        jPanel.setBackground(new Color(204, 228, 255));
        GridLayout gridLayout = new GridLayout(4,2);
        gridLayout.setVgap(15);
        gridLayout.setHgap(5);
        jPanel.setLayout(gridLayout);
        jPanel.setBorder(new EmptyBorder(30,50,30,50));

        add(jPanel);

        Font font = new Font(Font.DIALOG,Font.BOLD,17);
        JLabel jLabelCountry = new JLabel("Country: ");
        jLabelCountry.setFont(font);
        JTextField jTextFieldCountry = new JTextField("",5);
        this.jTextFieldCountry=jTextFieldCountry;
        jTextFieldCountry.setFont(font);
        jPanel.add(jLabelCountry);
        jPanel.add(jTextFieldCountry);

        JLabel jLabelCity = new JLabel("City: ");
        jLabelCity.setFont(font);
        JTextField jTextFieldCity = new JTextField("",5);
        this.jTextFieldCity=jTextFieldCity;
        jTextFieldCity.setFont(font);
        jPanel.add(jLabelCity);
        jPanel.add(jTextFieldCity);

        JLabel jLabelCurrency = new JLabel("Currency: ");
        jLabelCurrency.setFont(font);
        JTextField jTextFieldCurrency = new JTextField("",5);
        this.jTextFieldCurrency=jTextFieldCurrency;
        jTextFieldCurrency.setFont(font);
        jPanel.add(jLabelCurrency);
        jPanel.add(jTextFieldCurrency);

        JButton jButton = new JButton("OK");
        jButton.setFont(font);
        jPanel.add(jButton);
        jButton.addActionListener(this);


        setSize(400,300);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        service = new Service(jTextFieldCountry.getText());
        remove(getContentPane());


        JPanel jPanelMain = new JPanel();
        jPanelMain.setLayout(new BorderLayout());

        JPanel jPanelBottom = new JPanel();
        jPanelBottom.setLayout(new FlowLayout());
        jPanelMain.add(jPanelBottom,BorderLayout.PAGE_END);


        JLabel jLabelNBPRate = new JLabel("NBP rate: " + service.getNBPRate());
        jLabelNBPRate.setSize(new Dimension(100,70));
        jPanelBottom.add(jLabelNBPRate);

        JLabel jLabel = new JLabel("                   ");
        jPanelBottom.add(jLabel);

        JLabel jLabelExchangeRate = new JLabel("Exchange rate: "+service.getRateFor(jTextFieldCurrency.getText()));
        jPanelBottom.add(jLabelExchangeRate);


        String s = service.getWeather(jTextFieldCity.getText());
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(s);
            Map map = (Map) jsonObject.get("main");
            JLabel jLabelWeather = new JLabel("Weather: " + "temp: " + map.get("temp") + ",  pressure: " + map.get("pressure") +",  humidity: " +map.get("humidity") );
            jPanelMain.add(jLabelWeather,BorderLayout.PAGE_START);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        JFXPanel jfxPanel = service.getCity();
        jPanelMain.add(jfxPanel,BorderLayout.CENTER);

        setContentPane(jPanelMain);
        validate();



    }
}
