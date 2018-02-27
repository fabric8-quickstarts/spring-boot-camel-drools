/**
 *  Copyright 2005-2016 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.quickstarts.camel.drools;

import com.thoughtworks.xstream.XStream;
import io.fabric8.quickstarts.camel.drools.model.Person;
import org.apache.camel.dataformat.xstream.XStreamDataFormat;
import org.apache.camel.spi.DataFormatFactory;
import org.kie.internal.runtime.helper.BatchExecutionHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DecisionServerAutoConfiguration {

    private static final String HELLO_RULES_PACKAGE_NAME = "org.openshift.quickstarts.decisionserver.hellorules";

    /**
     * Adding a customized XStream data-format to the registry.
     * @return a kie-compatible XStream data format
     */
    @Bean(name = "xstream-dataformat")
    public XStreamDataFormat xStreamDataFormat() {
        XStream xstream = BatchExecutionHelper.newXStreamMarshaller();
        // Use the "model" package instead of the one used on the kie server
        xstream.aliasPackage(HELLO_RULES_PACKAGE_NAME, Person.class.getPackage().getName());

        return new XStreamDataFormat(xstream);
    }

    /**
     * Camel requires a dataformat factory as template to create dataformats.
     * Since we don't customize the xstream dataformat in the routes,
     * we can reuse the same instance in all invocations.
     */
    @Bean(name = "xstream-dataformat-factory")
    public DataFormatFactory dataFormatFactory(XStreamDataFormat xStreamDataFormat) {
        return () -> xStreamDataFormat;
    }

}
