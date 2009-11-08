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
public class SelectedBehavior implements ModifyingTagBehavior {
   Expression expression;

   public SelectedBehavior(Expression expression) {
      this.expression = expression;
   }

   @Override
   public void modify(Map<String, Object> properties, ModifyableTag tag) throws ExpressionEvaluationException {
      if(expression.asBoolean(properties)) {
         tag.getTagParts().add(new SimpleAttribute("selected", "selected"));
      }
   }

   public static BehaviorProvider<SelectedBehavior> getProvider() {
      return new BehaviorProvider<SelectedBehavior>() {
         @Override
         public SelectedBehavior get(DynamicAttribute keyAttribute, Map<AttributeKey, Attribute> attributes) throws ExpressionParsingException, BehaviorInstantiationException {
            return new SelectedBehavior(keyAttribute.getExpression());
         }
      };
   }
}
