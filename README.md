# DOT_MonteCarlo_Simulation
A MonteCarlo simulation of Diffuse Optical Tomography: simulating the movement, absorption, and scattering of photos in homogenous or heterogenous medium.
Each photon is individually simulated, until it is outside of the set boundary, or a given length of time has expired. This was implemented in Java

-Main: which instantiates the major classes, runs the simulation, and collects data
-MonteCarloVoxelPhotonTracker: which simulates the photon’s movement and interactions in each case
-ParticlePhoton: which is a representation of a single photon packet, which can propogate through the medium, rotate, scatter, or be partially absorbed via the Beer-Lambert Law. Scattering of the photon was done via Rayleigh scattering, which is described in its own class.
-OpticalVoxelMesh: which is a representation of a structured cube-mesh. It is made of a multi-dimensional array of OpticalMedia, which describe the local optical properties, such as the index of refraction, µs and µa. This allows the voxel mesh to support heterogenous media, such as tumors, bones, and other tissues.

To aid in calculations, several secondary classes and interfaces were written as follows:
-Vector: a class to represent a vector in 3D space. It was used to manage the position and direction of the photon, as well as describe the bounding box of the mesh.
-IEquation: an interface for a generic equation that can be evaluated at a given X value. One implementation of an equation was an array equation, which was used to invert the CDF of a mathematical distribution.
-Distribution: an interface for different distributions used. This included the Rayleigh scattering phase function.


Due to complications in debugging in secondary parts of the code, no output could be retrieved for verification that the main part of the simulation works as intended. The derivations of and logic of the simulation are given below. The code was modified to avoid buggy methods in the VoxelMesh class to ensure that the entire program runs, however no useful information can be provided. Some methods in MonteCarloVoxelPhotonTracker are commented out, with simplified lines provided below them. With all bugs removed, the commented lines would be running instead.

Main.java
	The Main class is intended to be run from the command line, and to instantiate the key classes, then run the simulation. It this case, homogenous media was used as instructed, however it contains the capabilities of adding heterogeneity. Once the media was created, it was loaded into the constructor for the voxel mesh. The voxel mesh was formed as a cube, 20mm X 20mm X 20mm in dimensions. For this case, each voxel was set to be 1mm in length, however each of the x, y, and z lengths could be varied. Finally, the particle tracker was instantiated using the mesh, a starting position and direction of the photon, the number of iterations to set, and the max time before killing each iteration.  Once the simulation is done, the desired data was written to a .csv file to be read with excel or python for rapid visualization. As debugging was prioritized, the visualization code is not provided. 
	MonteCarloVoxelPhotonTracker.java
	The Tracker Class contains only two methods, one which runs the simulation, and the next which collects data to send it to the main method. The run method contains three major loops. The first loops over every photon determined sufficient for the simulation. It instantiates the photon at the same position and direction and starts the simulation. 
	The second major loop is a while loop that keeps track of the time in the simulation. As the photon propagates through the media, the current time in the simulation is updated according to the time it takes the photon to travel across each individual voxel. Once the max specified time is met, the while loop ends for the next photon to start its simulation. In this implementation, time-invariant look at the light attenuation in the media was desired, however this class is flexible enough that with modification, it could easily track attenuation at different points of time by extending the voxel mesh class to consider the current time in the simulation.
	The third major loop manages the activities of the photon while it is between scatterers. Before it, a scattering length is set according to the following formula:
L= -ln⁡(U01)* µ_s
The length was then incrementally reduced as the photon travelled through each voxel. While in the loop the program did the following:

  - Check if the photon is outside the mesh, if so end this simulation and move on to the next photo
  - Get the current cell in the mesh that the photon is in, and propagate the photon through it. It uses the given method to calculate the length through the voxel. Packet weight is lost from the photon and accumulated in that specific voxel according to the Beer-Lambert law. The time of the simulation is updated by how long it would take the photon to travel through the voxel.
  - Check if the weight of the photon is below an arbitrarily set value, and if so run through a Russian Roulette method.
  - Check if the index of refraction of the last voxel is different than the index of refraction of the current voxel. This implementation was not completed, but if that was the case, then it would call a method to reassign a new direction of the photon depending on if it is reflected or transmitted.

Once the scattering length is reduced down to 0, the photon is scattered again. Scattering was done via Rayleigh scattering. 
A cumulative distribution function was created by integrating the 

ParticlePhoton.java
The photon was rotated by a calculated azimuthal and zenith angle. The azimuthal angle was calculated as shown in the slides. The zenith angle determination was done as follows:
The equation for the Rayleigh Phase Function of scattering is as:
P(θ)=3/4(1+〖cos⁡θ〗^2)
The phase function can mapped to a cumulative distribution function according to the following equation:
F(x)=P(X≤x)= ∫_0^x▒P(X)dx
The cumulative distribution function can then be inverted. This was done using the ArrayEquation class. The value of the CDF from 0 to Pi was with an array, and a second array of the same x values was provided to the ArrayEquation class. It then reflected the x and y values to create an inverse representation of the function. The inverted function could then be evaluated at some random uniform variable between 0 and 1 to provide an evaluation of the Rayleigh phase function.

OpticalVoxelMesh.java
Throughout the photonTracker class, the simulation calls upon the Optical Voxel Mesh to return the optical properties of each cell. The cells were described as a multidimensional array of an opticalproperty class. An array could be called as a key  to look up which voxel could describe where the photon was in, and the key could then be passed into other methods to pass a reference to the specific cell, which could then be modulated.
