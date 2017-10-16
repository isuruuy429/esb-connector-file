/*
 *  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.connector.unitTest.FileConnector;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.util.UUIDGenerator;
import org.apache.axiom.util.UIDGenerator;
import org.apache.axis2.AxisFault;
import org.apache.axis2.builder.BuilderUtil;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.OperationContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.description.InOutAxisOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseException;
import org.apache.synapse.config.SynapseConfiguration;
import org.apache.synapse.core.axis2.Axis2MessageContext;
import org.apache.synapse.mediators.template.TemplateContext;
//import org.mockito.Mock;
//import org.powermock.core.classloader.annotations.PrepareForTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.wso2.carbon.connector.ReadALine;

import java.nio.file.Paths;
import java.util.Stack;

//import static org.mockito.Matchers.anyString;
//import static org.mockito.MockitoAnnotations.initMocks;
//import static org.powermock.api.mockito.PowerMockito.mock;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Test class for GetSize.java.
 */
//@PrepareForTest({BuilderUtil.class})
public class ReadALineTest{
    private ReadALine readALine;

//    @Mock
//    Axis2MessageContext axis2MessageContext;

    private SynapseConfiguration synapseConfig;
    private MessageContext ctx;
    private ConfigurationContext configContext;

    public static String getFilePath(String fileName) {
        if (StringUtils.isNotBlank(fileName)) {
            return Paths
                    .get(System.getProperty("framework.resource.location"), "sampleFiles", fileName)
                    .toString();
        }
        return null;
    }

    @BeforeMethod
    public void setUp() throws Exception {
        readALine = new ReadALine();
        ctx = createMessageContext();
//        initMocks(this);
    }

//    /**
//     * Test case for readALine.
//     */
//    @Test
//    public void testConnect() throws AxisFault {
//        TemplateContext templateContext = new TemplateContext("fileConnector", null);
//        templateContext.getMappedValues().put("source", getFilePath("in/sampleFile.txt"));
//        templateContext.getMappedValues().put("lineNumber", getFilePath("1"));
//        templateContext.getMappedValues().put("setTimeout", "");
//        templateContext.getMappedValues().put("setPassiveMode", "");
//        templateContext.getMappedValues().put("setUserDirIsRoot", "");
//        templateContext.getMappedValues().put("setSoTimeout", "");
//        templateContext.getMappedValues().put("setStrictHostKeyChecking", "");
//
//        Stack<TemplateContext> fileStack = new Stack<>();
//        fileStack.push(templateContext);
//        ctx.setProperty("_SYNAPSE_FUNCTION_STACK", fileStack);
//        org.apache.axis2.context.MessageContext axis2MessageContext = ((Axis2MessageContext) ctx).getAxis2MessageContext();
//        mockStatic(BuilderUtil.class);
//        when(BuilderUtil.getBuilderFromSelector("text/plain", axis2MessageContext)).thenReturn(null);
//        readALine.connect(ctx);
//        System.out.println("3#######33  " + ctx.getEnvelope().getBody());
//        Assert.assertEquals(ctx.getEnvelope().getBody(), "This file is created for testing purpose.");
//    }

    /**
     * Test case for readALine with non existing file.
     */
    @Test(expectedExceptions = SynapseException.class)
    public void testConnectWithNonExistingFile(){
        TemplateContext templateContext = new TemplateContext("fileConnector", null);
        templateContext.getMappedValues().put("source", getFilePath("in/no.txt"));
        templateContext.getMappedValues().put("setTimeout", "");
        templateContext.getMappedValues().put("setPassiveMode", "");
        templateContext.getMappedValues().put("setUserDirIsRoot", "");
        templateContext.getMappedValues().put("setSoTimeout", "");
        templateContext.getMappedValues().put("setStrictHostKeyChecking", "");

        Stack<TemplateContext> fileStack = new Stack<>();
        fileStack.push(templateContext);
        ctx.setProperty("_SYNAPSE_FUNCTION_STACK", fileStack);
        readALine.connect(ctx);
    }

    private MessageContext createMessageContext() throws AxisFault {
        MessageContext msgCtx = createSynapseMessageContext();
        org.apache.axis2.context.MessageContext axis2MsgCtx = ((Axis2MessageContext) msgCtx).getAxis2MessageContext();
        axis2MsgCtx.setServerSide(true);
        axis2MsgCtx.setMessageID(UUIDGenerator.getUUID());
        return msgCtx;
    }

    private MessageContext createSynapseMessageContext() throws AxisFault {
        org.apache.axis2.context.MessageContext axis2MC = new org.apache.axis2.context.MessageContext();
        axis2MC.setConfigurationContext(this.configContext);
        ServiceContext svcCtx = new ServiceContext();
        OperationContext opCtx = new OperationContext(new InOutAxisOperation(), svcCtx);
        axis2MC.setServiceContext(svcCtx);
        axis2MC.setOperationContext(opCtx);
        Axis2MessageContext mc = new Axis2MessageContext(axis2MC, this.synapseConfig, null);
        mc.setMessageID(UIDGenerator.generateURNString());

        mc.setEnvelope(OMAbstractFactory.getSOAP12Factory().createSOAPEnvelope());
        mc.getEnvelope().addChild(OMAbstractFactory.getSOAP12Factory().createSOAPBody());

        return mc;
    }
}
