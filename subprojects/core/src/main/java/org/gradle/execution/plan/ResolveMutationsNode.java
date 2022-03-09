/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
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

package org.gradle.execution.plan;

import org.gradle.api.Action;
import org.gradle.api.internal.project.ProjectInternal;
import org.gradle.api.internal.tasks.NodeExecutionContext;
import org.gradle.internal.resources.ResourceLock;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class ResolveMutationsNode extends Node implements SelfExecutingNode {
    private final LocalTaskNode node;

    public ResolveMutationsNode(LocalTaskNode node) {
        this.node = node;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return "Resolve mutations for " + node;
    }

    @Override
    public int compareTo(Node o) {
        return -1;
    }

    @Nullable
    @Override
    public Throwable getNodeFailure() {
        return null;
    }

    @Override
    public void rethrowNodeFailure() {
    }

    @Override
    public void resolveDependencies(TaskDependencyResolver dependencyResolver, Action<Node> processHardSuccessor) {
        // This node must run after the dependencies of the node have completed
        for (Node node : node.getDependencySuccessors()) {
            if (node == this) {
                continue;
            }
            addDependencySuccessor(node);
            processHardSuccessor.execute(node);
        }
    }

    @Override
    public void resolveMutations() {
    }

    @Nullable
    @Override
    public ResourceLock getProjectToLock() {
        return node.getProjectToLock();
    }

    @Nullable
    @Override
    public ProjectInternal getOwningProject() {
        return node.getOwningProject();
    }

    @Override
    public List<? extends ResourceLock> getResourcesToLock() {
        return Collections.emptyList();
    }

    @Override
    public void execute(NodeExecutionContext context) {
    }
}
