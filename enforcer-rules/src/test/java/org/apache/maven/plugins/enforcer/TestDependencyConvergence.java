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

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilder;
import org.apache.maven.shared.dependency.graph.DependencyCollectorBuilderException;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.codehaus.plexus.PlexusContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@RunWith( Enclosed.class )
public class TestDependencyConvergence {

    public static class DoNotAllowSameArtifactDifferentVersion
    {
        private DependencyConvergenceTestSetup setup;

        @Before
        public void beforeMethod()
                throws IOException
        {
            this.setup = new DependencyConvergenceTestSetup();
        }

        @Test(expected = EnforcerRuleException.class)
        public void testSameArtifactDifferentVersions()
                throws Exception
        {
            this.setup.runRule( );
        }

        @Test
        public void testGroupIdArtifactIdVersion()
                throws Exception
        {
            Artifact artifact = new DefaultArtifact( "groupId", "artifactId", "version", "compile", "jar",
                    "classifier", null );
            final DefaultDependencyNode node = new DefaultDependencyNode( artifact );
            node.setChildren( Collections.emptyList() );

            this.setup.runRule(node );
        }
    }
}
