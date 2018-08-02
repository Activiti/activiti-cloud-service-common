/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.starters.test.builder;

import java.util.UUID;

import org.activiti.cloud.starters.test.EventsAggregator;
import org.activiti.runtime.api.event.impl.CloudTaskAssignedEventImpl;
import org.activiti.runtime.api.event.impl.CloudTaskCandidateUserAddedEventImpl;
import org.activiti.runtime.api.event.impl.CloudTaskCompletedEventImpl;
import org.activiti.runtime.api.event.impl.CloudTaskCreatedEventImpl;
import org.activiti.runtime.api.model.ProcessInstance;
import org.activiti.runtime.api.model.Task;
import org.activiti.runtime.api.model.impl.TaskCandidateUserImpl;
import org.activiti.runtime.api.model.impl.TaskImpl;

public class TaskEventContainedBuilder {

    private EventsAggregator eventsAggregator;

    public TaskEventContainedBuilder(EventsAggregator eventsAggregator) {
        this.eventsAggregator = eventsAggregator;
    }

    public Task aCreatedTask(String taskName,
                             ProcessInstance processInstance) {
        Task task = buildTask(taskName,
                              Task.TaskStatus.CREATED,
                              processInstance);
        eventsAggregator.addEvents(new CloudTaskCreatedEventImpl(task));
        return task;
    }

    public Task aCreatedStandaloneTaskWithParent(String taskName) {
        Task task = buildTask(taskName,
                              Task.TaskStatus.CREATED,
                              null);
        ((TaskImpl) task).setParentTaskId(UUID.randomUUID().toString());
        eventsAggregator.addEvents(new CloudTaskCreatedEventImpl(task));
        return task;
    }

    public Task anAssignedTask(String taskName,
                               String username,
                               ProcessInstance processInstance) {
        TaskImpl task = buildTask(taskName,
                                  Task.TaskStatus.ASSIGNED,
                                  processInstance);
        task.setAssignee(username);

        eventsAggregator.addEvents(new CloudTaskCreatedEventImpl(task),
                                   new CloudTaskAssignedEventImpl(task));
        return task;
    }

    public Task aCompletedTask(String taskName,
                               ProcessInstance processInstance) {
        Task task = buildTask(taskName,
                              Task.TaskStatus.COMPLETED,
                              processInstance);
        eventsAggregator.addEvents(new CloudTaskCreatedEventImpl(task),
                                   new CloudTaskAssignedEventImpl(task),
                                   new CloudTaskCompletedEventImpl(task));
        return task;
    }

    public Task aTaskWithUserCandidate(String taskName,
                                       String username,
                                       ProcessInstance processInstance) {
        TaskImpl task = buildTask(taskName,
                                  Task.TaskStatus.CREATED,
                                  processInstance);
        eventsAggregator.addEvents(new CloudTaskCreatedEventImpl(task),
                                   new CloudTaskCandidateUserAddedEventImpl(new TaskCandidateUserImpl(username,
                                                                                                      task.getId())));
        return task;
    }

    private static TaskImpl buildTask(String taskName,
                                      Task.TaskStatus status,
                                      ProcessInstance processInstance) {
        TaskImpl task = new TaskImpl(UUID.randomUUID().toString(),
                                     taskName,
                                     status);
        if(processInstance != null) {
            task.setProcessInstanceId(processInstance.getId());
        }
        return task;
    }
}