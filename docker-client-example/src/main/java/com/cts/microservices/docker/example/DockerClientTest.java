package com.cts.microservices.docker.example;

import java.util.List;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ImageSearchResult;

public class DockerClientTest {
	
	public static void main(String args[]){
		
		DockerClientTest dockerClientTest = new DockerClientTest();
		dockerClientTest.startContainerImage();
		
	}
	
	private void startContainerImage(){
		
		try {
			//final DockerHost DOCKER_HOST = DockerHost.fromEnv(); 
			//final DockerClient docker = new DefaultDockerClient(DOCKER_HOST.uri());
			final DockerClient docker = DefaultDockerClient.fromEnv().build();
			
			System.out.println("Created dockerClient");
			//18272ef2b0c7
			final List<ImageSearchResult> searchResult = docker.searchImages("screening-service/screening-service");
			System.out.println("searchResult list"+ searchResult.size());
			
			docker.pull("18272ef2b0c7");
			System.out.println("Pulled Image screening-service");
			docker.close();
			
//			final List<Container> containers = docker.listContainers();
//			System.out.println("got containers list"+ containers.size());
//			
//			final List<Image> images = docker.listImages();
//			System.out.println("got images list"+ images.size());
//			for(Image image: images){
//				System.out.println("Image Id:"+image.id());
//				//System.out.println("Image Lable:"+image.labels());
//				//System.out.println("Image RepoTags:"+image.repoTags());
//				System.out.println("Image:"+image);
//			}
			

//			
//			// Bind container ports to host ports
//			final String[] ports = {"9080", "9082"};
//			final Map<String, List<PortBinding>> portBindings = new HashMap<>();
//			for (String port : ports) {
//			    List<PortBinding> hostPorts = new ArrayList<>();
//			    hostPorts.add(PortBinding.of("0.0.0.0", port));
//			    portBindings.put(port, hostPorts);
//			}
//			
//			final HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
//			final ContainerConfig containerConfig = ContainerConfig.builder()
//		            .hostConfig(hostConfig)
//		            .image("9c72313b6518").exposedPorts(ports)
//		            .build();
//			
//			System.out.println("Created ContainerConfig");
//			final ContainerCreation creation = dockerClient.createContainer(containerConfig);
//		    final String id = creation.id();
//
//		    // Start container
//		    dockerClient.startContainer(id);
//		    System.out.println("Started container");
//
//		    final List<Container> containers = dockerClient.listContainers();
//		    for(Container container: containers){
//		    	System.out.println("Container Image Id: "+container.imageId());
//		    }
//		    
//		    dockerClient.close();

		}
		catch(Exception exception){
			exception.printStackTrace();
			//System.out.println("");
		}
	}

}
