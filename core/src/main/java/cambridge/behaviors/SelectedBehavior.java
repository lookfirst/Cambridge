package cambridge.behaviors;

import cambridge.*;
import cambridge.parser.expressions.Expression;
import cambridge.model.ModifyableTag;
import cambridge.model.SimpleAttribute;
import cambridge.model.DynamicAttribute;
import cambridge.model.Attribute;

import java.util.Map;

/**
 * User: erdinc
 * Date: Nov 7, 2009
 * Time: 8:36:59 PM
 */
public class SelectedBehavior extends ModifyingTagBehavior {
   private final Expression expression;

   public SelectedBehavior(Expression expression) {
      this.expression = expression;
   }

   public void modify(Map<String, Object> bindings, ModifyableTag tag) throws ExpressionEvaluationException {
      if(expression.asBoolean(bindings)) {
         tag.getTagParts().add(new SimpleAttribute("selected", "selected"));
      }
   }

   public static BehaviorProvider<SelectedBehavior> getProvider() {
      return new BehaviorProvider<SelectedBehavior>() {
         public SelectedBehavior get(DynamicAttribute keyAttribute, Map<AttributeKey, Attribute> attributes) throws ExpressionParsingException, BehaviorInstantiationException {
            return new SelectedBehavior(keyAttribute.getExpression());
         }
      };
   }
}
