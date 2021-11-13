package org.apache.maven.plugins.enforcer;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.apache.maven.plugin.testing.ArtifactStubFactory;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilder;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilderException;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class DependencyConvergenceTestSetup {
    public DependencyConvergenceTestSetup() {
        this.includes = new ArrayList<String>();

        ArtifactStubFactory factory = new ArtifactStubFactory();

        MockProject project = new MockProject();
        project.setArtifacts( Collections.emptySet());
        project.setDependencyArtifacts( Collections.emptySet());

//        this.helper = EnforcerTestUtils.getHelper( project );

        this.log = mock(Log.class);

        this.helper = spy(EnforcerTestUtils.getHelper( project ));

        doReturn(log).when(helper).getLog();

        this.rule = new DependencyConvergence();

        project.setArtifacts( Collections.emptySet());
        project.setDependencyArtifacts( Collections.emptySet());
    }

    private List<String> includes;

    private DependencyConvergence rule;

    private EnforcerRuleHelper helper;

    private Log log;

    public EnforcerRuleHelper getHelper( ) {
        return helper;
    }


    public void runRule( )
            throws EnforcerRuleException
    {
        rule.execute( helper );
    }

    static class Log extends SystemStreamLog {
        private StringBuilder warnBuilder = new StringBuilder();

        public String getWarn() {
            return warnBuilder.toString();
        }

        @Override
        public void warn(CharSequence content) {
            warnBuilder.append(content);
        }
    }

    public void runRule(final DefaultDependencyNode node)
            throws EnforcerRuleException, ComponentLookupException {
        DependencyCollectorBuilder dependencyCollectorBuilder = new DependencyCollectorBuilder() {
            @Override
            public org.apache.maven.shared.dependency.graph.DependencyNode collectDependencyGraph( ProjectBuildingRequest buildingRequest,
                                                                                                   ArtifactFilter filter )
                    throws DependencyCollectorBuilderException
            {
                return node;
            }
        };

        Mockito.when( helper.getContainer().lookup( DependencyCollectorBuilder.class ) ).thenReturn( dependencyCollectorBuilder  );

        rule.execute( helper );
    }
}
