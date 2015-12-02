package com.chairbender.object_calisthenics_analyzer.intellij.ui;

import com.chairbender.object_calisthenics_analyzer.intellij.ui.action.ViolationLinkAction;
import com.chairbender.object_calisthenics_analyzer.violation.Violation;
import com.chairbender.object_calisthenics_analyzer.violation.ViolationMonitor;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Jcomponent for displaying a report
 */
public class ReportComponent extends JScrollPane {

    /**
     *
     * @param violationMonitor monitor whose reports should be displayed
     * @param inProject project in which the analysis was performed
     */
    public ReportComponent(ViolationMonitor violationMonitor, Project inProject) throws BadLocationException {
        super();

        final JTextPane textPane = new JTextPane();
        final StyledDocument styledDocument = textPane.getStyledDocument();
        super.setViewportView(textPane);
        textPane.setEditable(false);


        for (Violation violation : violationMonitor.getAllViolations()) {
            Style regularBlue = styledDocument.addStyle("regularBlue", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));
            StyleConstants.setForeground(regularBlue, Color.BLUE);
            StyleConstants.setUnderline(regularBlue, true);
            regularBlue.addAttribute("linkact", new ViolationLinkAction(violation, inProject));
            styledDocument.insertString(styledDocument.getLength(), violation.getRuleInfo().describe() + "\n\t",null);
            styledDocument.insertString(styledDocument.getLength(),violation.toString() + "\n", regularBlue);
        }

        textPane.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Element ele = styledDocument.getCharacterElement(textPane.viewToModel(e.getPoint()));
                AttributeSet as = ele.getAttributes();
                ViolationLinkAction violationLinkAction = (ViolationLinkAction) as.getAttribute("linkact");
                if (violationLinkAction != null) {
                    textPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                } else {
                    textPane.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
            }
        });

        textPane.addMouseListener(new MouseListener() {


            @Override
            public void mouseClicked(MouseEvent e) {
                Element ele = styledDocument.getCharacterElement(textPane.viewToModel(e.getPoint()));
                AttributeSet as = ele.getAttributes();
                ViolationLinkAction violationLinkAction = (ViolationLinkAction) as.getAttribute("linkact");
                if (violationLinkAction != null) {
                    violationLinkAction.goToViolation();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

    }
}