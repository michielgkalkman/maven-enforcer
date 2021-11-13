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
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.enforcer.rule.api.EnforcerRuleHelper;
import org.apache.maven.shared.dependency.graph.internal.DefaultDependencyNode;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@RunWith( Enclosed.class )
public class TestDependencyConvergence {

    public static class DoNotAllowSameArtifactDifferentVersion
    {
        private DependencyConvergenceTestSetup setup;

        @Before
        public void beforeMethod()
        {
            this.setup = new DependencyConvergenceTestSetup();
        }

        @Test(expected = EnforcerRuleException.class)
        public void testSameArtifactDifferentVersions()
                throws Exception
        {
            final EnforcerRuleHelper helper = this.setup.getHelper();
            try {
                this.setup.runRule();
            } finally {
                verify(helper.getLog()).warn(String.format("%n" +
                        "Dependency convergence error for groupId:artifact:jar:1.0.0:compile paths to dependency are:%n" +
                        "+-groupId:artifactId:jar:classifier:version:compile%n" +
                        "  +-groupId:artifact:jar:1.0.0:compile%n" +
                        "and%n" +
                        "+-groupId:artifactId:jar:classifier:version:compile%n" +
                        "  +-groupId:artifact:jar:2.0.0:compile%n"));
            }
        }

        @Test
        public void testGroupIdArtifactIdVersion() throws EnforcerRuleException, ComponentLookupException {
            Artifact artifact = new DefaultArtifact( "groupId", "artifactId", "version", "compile", "jar",
                    "classifier", null );
            final DefaultDependencyNode node = new DefaultDependencyNode( artifact );
            node.setChildren( Collections.emptyList() );

            final EnforcerRuleHelper helper = this.setup.getHelper();

            this.setup.runRule(node);

            verifyNoInteractions(helper.getLog());
        }
    }
}
