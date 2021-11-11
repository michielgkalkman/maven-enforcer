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

import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.testing.ArtifactStubFactory;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuildingRequest;
import org.mockito.internal.util.collections.HashCodeAndEqualsSafeSet;

import java.io.IOException;
import java.util.*;

public class DependencyConvergenceTestSetup {
    public DependencyConvergenceTestSetup() throws IOException {
        this.includes = new ArrayList<String>();

        ArtifactStubFactory factory = new ArtifactStubFactory();

        MockProject project = new MockProject();
        project.setArtifacts( Collections.emptySet());
        project.setDependencyArtifacts( Collections.emptySet());

        this.helper = EnforcerTestUtils.getHelper( project );

        this.rule = new DependencyConvergence();
    }

    private List<String> includes;

    private DependencyConvergence rule;

    private EnforcerRuleHelper helper;

    public void runRule( )
            throws EnforcerRuleException
    {
        rule.execute( helper );
    }
}
