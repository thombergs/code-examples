package com.myorg;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.ec2.BlockDeviceVolume;
import software.amazon.awscdk.services.ec2.BlockDevice;
import software.amazon.awscdk.services.ec2.IMachineImage;
import software.amazon.awscdk.services.ec2.IVpc;
import software.amazon.awscdk.services.ec2.Instance;
import software.amazon.awscdk.services.ec2.InstanceClass;
import software.amazon.awscdk.services.ec2.InstanceSize;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.MachineImage;
import software.amazon.awscdk.services.ec2.SecurityGroup;
import software.amazon.awscdk.services.ec2.Vpc;
import software.amazon.awscdk.services.ec2.VpcLookupOptions;
import software.constructs.Construct;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class CdkAppStack extends Stack {
    public CdkAppStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public CdkAppStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        
        
		
        VpcLookupOptions options = VpcLookupOptions.builder().isDefault(true).build();
		IVpc vpc = Vpc.fromLookup(this, "vpc", options);
       
        
        SecurityGroup securityGroup = SecurityGroup
        		.Builder
        		.create(this, "sg")
        		.vpc(vpc)
        		.allowAllOutbound(true)
        		.build();
        
        

        Instance.Builder.create(this, "Instance")
		        .vpc(vpc)
		        .instanceType(InstanceType.of(InstanceClass.BURSTABLE2, InstanceSize.MICRO))
		        .machineImage(MachineImage.latestAmazonLinux())
		        .blockDevices(List.of(BlockDevice.builder()
		                .deviceName("/dev/sda1")
		                .volume(BlockDeviceVolume.ebs(50))
		                .build(), BlockDevice.builder()
		                .deviceName("/dev/sdm")
		                .volume(BlockDeviceVolume.ebs(100))
		                .build()))
		        .securityGroup(securityGroup)
		        .build();
        
    }
}
