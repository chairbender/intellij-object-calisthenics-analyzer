package com.chairbender.object_calisthenics_analyzer.intellij.ui;

import com.chairbender.object_calisthenics_analyzer.intellij.ui.action.ViolationLinkAction;
import com.chairbender.object_calisthenics_analyzer.violation.Violation;
import com.chairbender.object_calisthenics_analyzer.violation.ViolationMonitor;
import com.chairbender.object_calisthenics_analyzer.violation.model.ViolationCategory;
import com.intellij.designer.PaletteToolWindowContent;
import com.intellij.ide.ui.UISettings;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorColorsUtil;
import com.intellij.openapi.fileEditor.impl.EditorTabColorProvider;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.UI;
import com.intellij.ui.components.labels.LinkLabel;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

        EditorColorsScheme editorColorsScheme = EditorColorsUtil.getGlobalOrDefaultColorScheme();
        final JTextPane textPane = new JTextPane();
        final StyledDocument styledDocument = textPane.getStyledDocument();
        super.setViewportView(textPane);
        UIUtil.setBackgroundRecursively(this, editorColorsScheme.getDefaultBackground());

        Map<ViolationCategory,List<Violation>> violations = violationMonitor.getAllViolations();
        ArrayList<ViolationCategory> violationCategories = new ArrayList<ViolationCategory>();
        violationCategories.addAll(violations.keySet());
        Collections.sort(violationCategories);
        SimpleAttributeSet normalTextAttributes = new SimpleAttributeSet();
        StyleConstants.setFontFamily(normalTextAttributes, editorColorsScheme.getConsoleFontName());
        StyleConstants.setFontSize(normalTextAttributes, editorColorsScheme.getConsoleFontSize());

        for (ViolationCategory violationCategory : violationCategories) {

            styledDocument.insertString(styledDocument.getLength(), violationCategory.getRuleInfo().describe() + "\n", normalTextAttributes);
            for (Violation violation : violations.get(violationCategory)) {
                Style linkStyle = styledDocument.addStyle("regularBlue", StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE));
                StyleConstants.setForeground(linkStyle, UI.getColor("link.foreground"));
                StyleConstants.setFontSize(linkStyle, editorColorsScheme.getConsoleFontSize());
                StyleConstants.setFontFamily(linkStyle, editorColorsScheme.getConsoleFontName());
                StyleConstants.setUnderline(linkStyle, true);
                linkStyle.addAttribute("linkact", new ViolationLinkAction(violation, inProject));
                styledDocument.insertString(styledDocument.getLength(), "\t", normalTextAttributes);
                styledDocument.insertString(styledDocument.getLength(),violation.toString() + "\n", linkStyle);
            }
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

        textPane.setCaretPosition(0);

    }
}
