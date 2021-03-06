package cambridge.parser.expressions;

import cambridge.ExpressionEvaluationException;
import cambridge.runtime.ExpressionContext;

/**
 * @author Erdinc Yilmazel
 * Date: Oct 31, 2009
 * Time: 1:07:43 AM
 */
public class StringLiteral implements CambridgeExpression {
   private final String value;

   public StringLiteral(String value) {
      this.value = value;
   }

   public static StringLiteral fromText(String value) {
      value = value.replaceAll("\\\\n", "\n").replaceAll("\\\\t", "\t").replaceAll("\\\\r", "\r");

      if (value.length() <= 2) {
         return new StringLiteral("");
      } else {
         return new StringLiteral(value.substring(1, value.length() - 1));
      }
   }

   public Type getType(ExpressionContext context) throws ExpressionEvaluationException {
      return Type.String;
   }

   public String eval(ExpressionContext context) {
      return value;
   }

   public boolean asBoolean(ExpressionContext context) throws ExpressionEvaluationException {
      return !value.equals("");
   }

   public int asInt(ExpressionContext context) throws ExpressionEvaluationException {
      try {
         return Integer.parseInt(value);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   public float asFloat(ExpressionContext context) throws ExpressionEvaluationException {
      try {
         return Float.parseFloat(value);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   public double asDouble(ExpressionContext context) throws ExpressionEvaluationException {
      try {
         return Double.parseDouble(value);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   public long asLong(ExpressionContext context) throws ExpressionEvaluationException {
      try {
         return Long.parseLong(value);
      } catch (NumberFormatException e) {
         return 0;
      }
   }

   public String asString(ExpressionContext context) throws ExpressionEvaluationException {
      return value;
   }

   public String toString() {
      return value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      StringLiteral that = (StringLiteral) o;

      return !(value != null ? !value.equals(that.value) : that.value != null);
   }

   @Override
   public int hashCode() {
      return value != null ? value.hashCode() : 0;
   }
}
