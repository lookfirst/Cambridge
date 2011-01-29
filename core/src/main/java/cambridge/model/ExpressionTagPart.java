package cambridge.model;

import cambridge.Cambridge;
import cambridge.ExpressionEvaluationException;
import cambridge.ExpressionParsingException;
import cambridge.TemplateEvaluationException;
import cambridge.parser.expressions.Expression;
import cambridge.parser.expressions.ExpressionLexer;
import cambridge.parser.expressions.ExpressionParser;
import cambridge.runtime.DefaultTemplateBindings;
import cambridge.runtime.Filter;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 * Represents cambridge expressions that are used inside an HTML tag.
 */
public class ExpressionTagPart implements TagPart, Fragment {
   String textContent;
   private Expression expression;

   ArrayList<F> filters;

   class F {
      final Filter filter;
      final String parameters;

      F(Filter filter, String parameters) {
         this.filter = filter;
         this.parameters = parameters;
      }
   }

   public void setFilters(ArrayList<String> f) {
      filters = new ArrayList<F>();
      for (String s : f) {
         String name;
         String params;

         int i = s.indexOf(':');
         if (i != -1) {
            name = s.substring(0, i);
            params = s.substring(i + 1).trim();
         } else {
            name = s;
            params = null;
         }

         Filter filter = Cambridge.getInstance().getFilter(name);
         if (filter != null) {
            filters.add(new F(filter, params));
         }
      }

      if (filters.size() == 0) {
         filters = null;
      }
   }

   public ExpressionTagPart(String textContent) throws ExpressionParsingException {
      this.textContent = textContent;

      try {
         ANTLRStringStream stream = new ANTLRStringStream(textContent);
         ExpressionLexer lexer = new ExpressionLexer(stream);
         TokenStream tokenStream = new CommonTokenStream(lexer);
         ExpressionParser parser = new ExpressionParser(tokenStream);
         expression = parser.compilationUnit();
         if (parser.getErrors() != null) {
            throw new ExpressionParsingException(textContent, parser.getErrors());
         }
      } catch (RecognitionException e) {
         throw new ExpressionParsingException(textContent, e);
      }
   }

   public boolean isWhiteSpace() {
      return false;
   }

   public void eval(Map<String, Object> bindings, Appendable out) throws IOException, TemplateEvaluationException {
      try {
         Object value = expression.eval(bindings);
         if (value != null) {
            Locale locale = (Locale) bindings.get(DefaultTemplateBindings.LocaleVariable);
            if (locale == null) {
               locale = Locale.getDefault();
            }
            out.append(applyFilters(value, locale));
         }
      } catch (ExpressionEvaluationException e) {
         throw new TemplateEvaluationException(e);
      }
   }

   public void pack() {
   }

   private String applyFilters(Object o, Locale locale) {
      if (filters == null) return o.toString();
      String val = "";
      for (int i = 0; i < filters.size(); i++) {
         F f = filters.get(i);
         if (i == 0) {
            val = f.filter.doFilter(o, f.parameters, locale);
         } else {
            val = f.filter.doFilter(val, f.parameters, locale);
         }
      }

      return val;
   }

   public String getTextContent() {
      return textContent;
   }

   public void setTextContent(String textContent) {
      this.textContent = textContent;
   }
}
