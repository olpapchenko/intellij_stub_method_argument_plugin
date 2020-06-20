// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package org.intellij.sdk.intention;

import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@NonNls
public class RandomArgumentsIntention extends PsiElementBaseIntentionAction implements IntentionAction {

    public static final String INT_TYPE = "int";
    public static final String STRING_TYPE = ".String";
    public static final String BIG_DECIMAL_TYPE = ".BigDecimal";
    public static final String LONG_TYPE = "long";
    public static final String DOUBLE_TYPE = "double";
    public static final String FLOAT_TYPE = "float";
    public static final String CHAR_TYPE = "char";
    public static final String BOOLEAN_TYPE = "boolean";
    public static final String SHORT_TYPE = "short";
    public static final String BYTE_TYPE = "byte";

    @NotNull
    public String getText() {
        return "Generate stub arguments";
    }


    /**
     * Returns text for name of this family of intentions. It is used to externalize
     * "auto-show" state of intentions.
     * It is also the directory name for the descriptions.
     *
     * @return the intention family name.
     */
    @NotNull
    public String getFamilyName() {
        return "StubMethodArguments";
    }

    /**
     * Checks whether this intention is available at the caret offset in file
     * @param project a reference to the Project object being edited.
     * @param editor  a reference to the object editing the project source
     * @param element a reference to the PSI element currently under the caret
     * @return <ul>
     * <li> true if the caret is in a literal string element, so this functionality
     * should be added to the intention menu.</li>
     * <li> false for all other types of caret positions</li>
     * </ul>
     */
    public boolean isAvailable(@NotNull Project project, Editor editor, @Nullable PsiElement element) {
        if (element == null) return false;
        return PsiTreeUtil.getParentOfType(element, PsiCallExpression.class, false) != null;
    }

    /**
     * Modifies the Psi to change a ternary expression to an if-then-else statement.
     * If the ternary is part of a declaration, the declaration is separated and
     * moved above the if-then-else statement. Called when user selects this intention action
     * from the available intentions list.
     *
     * @param project a reference to the Project object being edited.
     * @param editor  a reference to the object editing the project source
     * @param element a reference to the PSI element currently under the caret
     * @throws IncorrectOperationException Thrown by underlying (Psi model) write action context
     *                                     when manipulation of the psi tree fails.
     * @see RandomArgumentsIntention#startInWriteAction()
     */
    public void invoke(@NotNull Project project, Editor editor, @NotNull PsiElement element) throws IncorrectOperationException {
        PsiCallExpression call = PsiTreeUtil.getParentOfType(element, PsiCallExpression.class, false);
        if (call != null) {
            Document doc = editor.getDocument();
            PsiParameter[] params = call.resolveMethod().getParameterList().getParameters();
            int offset = editor.getCaretModel().getOffset();

            String parameters = "";

            String res = Arrays.stream(params).map(this::getValue).collect(Collectors.joining(", "));
            doc.insertString(offset, res);
            editor.getCaretModel().moveToOffset(offset + 1);
        }
    }

    private String getValue(PsiParameter parameter) {
        String parameterType = parameter.getType().getCanonicalText();
        if (BOOLEAN_TYPE.equals(parameterType)) {
            return "true";
        }
        if (BYTE_TYPE.equals(parameterType)) {
            return "(byte)1";
        }
        if (SHORT_TYPE.equals(parameterType)) {
            return "(short)1";
        }
        if (DOUBLE_TYPE.equals(parameterType)) {
            return "1.0";
        }
        if (FLOAT_TYPE.equals(parameterType)) {
            return "1.0f";
        }
        if (CHAR_TYPE.equals(parameterType)) {
            return "'c'";
        }
        if (LONG_TYPE.equals(parameterType)) {
            return "1L";
        }
        if (INT_TYPE.equals(parameterType)) {
            return "1";
        }
        if (parameterType.endsWith(STRING_TYPE)) {
            return "\"test" + parameter.getName().substring(0, 1).toUpperCase()
                    + parameter.getName().substring(1) + "\"";
        }
        if (parameterType.endsWith(BIG_DECIMAL_TYPE)) {
            return "BigDecimal.ONE";
        }
        return "null";
    }

    /**
     * Indicates this intention action expects the Psi framework to provide the write action
     * context for any changes.
     *
     * @return <ul>
     * <li> true if the intention requires a write action context to be provided</li>
     * <li> false if this intention action will start a write action</li>
     * </ul>
     */
    public boolean startInWriteAction() {
        return true;
    }


}

