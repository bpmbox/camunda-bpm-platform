/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.engine.impl.cmd;

import org.camunda.bpm.engine.history.UserOperationLogEntry;
import org.camunda.bpm.engine.impl.core.variable.scope.AbstractVariableScope;
import org.camunda.bpm.engine.impl.interceptor.Command;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Stefan Hentschel.
 */
public abstract class AbstractSetVariableCmd extends AbstractVariableCmd implements Command<Void>, Serializable {

  public AbstractSetVariableCmd(String entityId, Map<String, ?> variables, boolean isLocal) {
    super(entityId, variables, isLocal);
  }

  @Override
  public void executeOperation(AbstractVariableScope scope) {
    if (isLocal) {
      scope.setVariablesLocal(variables);
    } else {
      scope.setVariables(variables);
    }
  }

  @Override
  public String getLogEntryOperation() {
    return UserOperationLogEntry.OPERATION_TYPE_SET_VARIABLE;
  }
}
