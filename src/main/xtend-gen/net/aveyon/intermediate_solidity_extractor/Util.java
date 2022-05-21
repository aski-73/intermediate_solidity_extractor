package net.aveyon.intermediate_solidity_extractor;

import java.util.List;
import net.aveyon.intermediate_solidity.Field;
import net.aveyon.intermediate_solidity.Function;
import net.aveyon.intermediate_solidity.LocalField;
import net.aveyon.intermediate_solidity.Modifier;
import net.aveyon.intermediate_solidity.Visibility;
import org.eclipse.xtend2.lib.StringConcatenation;

@SuppressWarnings("all")
public class Util {
  public static String printFunctionKeyWords(final Function function) {
    StringConcatenation _builder = new StringConcatenation();
    String _printVisibility = Util.printVisibility(function.getVisibility());
    _builder.append(_printVisibility);
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      boolean _doesOverride = function.getDoesOverride();
      if (_doesOverride) {
        _builder_1.append(" override");
      }
    }
    String _plus = (_builder.toString() + _builder_1);
    StringConcatenation _builder_2 = new StringConcatenation();
    {
      boolean _isVirtual = function.isVirtual();
      if (_isVirtual) {
        _builder_2.append(" virtual");
      }
    }
    String _plus_1 = (_plus + _builder_2);
    StringConcatenation _builder_3 = new StringConcatenation();
    {
      boolean _payable = function.getPayable();
      if (_payable) {
        _builder_3.append(" payable");
      }
    }
    return (_plus_1 + _builder_3);
  }
  
  public static String printModifierKeyWords(final Modifier modifier) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _doesOverride = modifier.getDoesOverride();
      if (_doesOverride) {
        _builder.append(" override");
      }
    }
    StringConcatenation _builder_1 = new StringConcatenation();
    {
      boolean _isVirtual = modifier.isVirtual();
      if (_isVirtual) {
        _builder_1.append(" virtual");
      }
    }
    return (_builder.toString() + _builder_1);
  }
  
  public static String printFieldKeyWords(final Field field) {
    StringConcatenation _builder = new StringConcatenation();
    String _printLocalFieldKeyWords = Util.printLocalFieldKeyWords(field);
    _builder.append(_printLocalFieldKeyWords);
    _builder.append(" ");
    String _printVisibility = Util.printVisibility(field.getVisibility());
    _builder.append(_printVisibility);
    return _builder.toString();
  }
  
  public static String printLocalFieldKeyWords(final LocalField field) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append(field.type);
    {
      boolean _payable = field.getPayable();
      if (_payable) {
        _builder.append(" payable");
      }
    }
    return _builder.toString();
  }
  
  public static String printVisibility(final Visibility visibility) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((visibility != null)) {
        String _lowerCase = visibility.name().toLowerCase();
        _builder.append(_lowerCase);
      } else {
        _builder.append("public");
      }
    }
    _builder.newLineIfNotEmpty();
    return _builder.toString();
  }
  
  public static String printReturnValues(final List<String> returnValues) {
    if (((returnValues == null) || (returnValues.size() == 0))) {
      return "";
    }
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _hasElements = false;
      for(final String r : returnValues) {
        if (!_hasElements) {
          _hasElements = true;
        } else {
          _builder.appendImmediate(", ", "");
        }
        _builder.append(r);
      }
    }
    return _builder.toString();
  }
}
