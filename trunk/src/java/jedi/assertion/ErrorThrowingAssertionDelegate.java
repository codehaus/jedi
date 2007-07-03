package jedi.assertion;

public class ErrorThrowingAssertionDelegate implements AssertionDelegate {
    
    public void assertTrue(boolean value, String message, Object... context) {
        if (!value) {
            throw new AssertionError(message + getContextAsString(context));
        }
    }

    private String getContextAsString(Object... context) {
        if (context == null || context.length == 0) {
            return "";
        }

        StringBuffer sb = new StringBuffer(256);
        sb.append(": context {");
        append(sb, context[0]);
        for (int i = 1; i < context.length; i++) {
            sb.append(", ");
            append(sb, context[i]);
        }
        sb.append("}");

        return sb.toString();
    }

    private void append(StringBuffer sb, Object obj) {
        sb.append('[');
        try {
            sb.append(obj);
        } catch (Exception ex) {
            sb.append(nonNullToString(obj)).append(".toString() threw ")
                .append(ex.getClass().getName()).append("(\"").append(ex.getMessage()).append("\")");
        }
        sb.append(']');
    }

    private String nonNullToString(Object obj) {
        return obj.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(obj));
    }
}
