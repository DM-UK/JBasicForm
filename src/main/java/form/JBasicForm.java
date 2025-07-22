package form;

import slider.DoubleSlider;
import slider.SliderDisplayPane;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class JBasicForm extends JPanel {
    private List<ActionListener> actionListeners = new ArrayList<>();
    private JPanel labelPanel = new JPanel();
    private JPanel componentPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JButton button = new JButton();

    public JBasicForm(){
        init();
    }

    private void init() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

        labelPanel.setLayout(
                new GridLayout(0, 1));

        componentPanel.setLayout(
                new GridLayout(0, 1));

        add(labelPanel, BorderLayout.WEST);
        add(componentPanel, BorderLayout.CENTER);

        //hide button for forms that don't require one
        button.setVisible(false);
        buttonPanel.add(button);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public JButton getButton() {
        return button;
    }

    protected void setButtonText(String text) {
        button.setText(text);
        button.setVisible(true);
    }

    private void addLabel(String labelText) {
        JLabel label = new JLabel(labelText);
        labelPanel.add(label);
    }

    private void addField(JComponent component) {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.LEFT));
        container.add(component);
        componentPanel.add(container);
    }

    protected JTextField addTextField(String labelText, String defaultValue, int length){
        JTextField textField = new JTextField(defaultValue, length);
        attachListenerToTextField(textField);
        addLabel(labelText);
        addField(textField);
        return textField;
    }

    protected JSlider addSliderField(String labelText, int min, int max, int init){
        SliderDisplayPane sliderDisplayPane = new SliderDisplayPane(min, max, init);
        JSlider slider = sliderDisplayPane.getSlider();
        attachListenerToSlider(slider);
        addLabel(labelText);
        addField(sliderDisplayPane);
        return slider;
    }

    protected DoubleSlider addSliderField(String labelText, double min, double max, double init){
        SliderDisplayPane sliderDisplayPane = new SliderDisplayPane(min, max, init);
        DoubleSlider slider = sliderDisplayPane.getDoubleSlider();
        attachListenerToSlider(slider);
        addLabel(labelText);
        addField(sliderDisplayPane);
        return slider;
    }

    protected JComboBox<String> addComboBoxField(String labelText, String[] options){
        JComboBox<String> comboBox = new JComboBox<>(options);
        attachListenerToComboBox(comboBox);
        addLabel(labelText);
        addField(comboBox);
        return comboBox;
    }

    private void fireActionListeners() {
        ActionEvent event = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "valueChanged");

        for (ActionListener listener : actionListeners) {
            listener.actionPerformed(event);
        }
    }

    public void addActionListener(ActionListener listener) {
        actionListeners.add(listener);
    }

    public void removeActionListener(ActionListener listener) {
        actionListeners.remove(listener);
    }

    private void attachListenerToTextField(JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { fireActionListeners(); }
            public void removeUpdate(DocumentEvent e) { fireActionListeners(); }
            public void changedUpdate(DocumentEvent e) { fireActionListeners(); }
        });
    }

    private void attachListenerToComboBox(JComboBox<String> comboBox) {
        comboBox.addActionListener(e -> {
            fireActionListeners();
        });
    }

    private void attachListenerToSlider(JSlider slider) {
        slider.addChangeListener(e -> {
            fireActionListeners();
        });
    }
}
