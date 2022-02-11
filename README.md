# ASE Delivery

ASE Delivery project

How to run the project after having a new IP address:  
1. change the address in frontend project: ase-delivery-spa->src->api->index.js  
2. change the environment->CORS in docker-compose.yml file: for ase-delivery-cas, ase-delivery-dse, ase-delivery-gateway services  
3. change the public dns address in the hardware project: ase-delivery-hardware->api.py  
4. change the ci variables AWS_USER_HOST in setting->ci/cd->variables to the new user_host in the new aws instance   
5. remember to use the new ip to access website in the browser...  

<br>

And if there's no active gitlab-runner:  
Use the code to register a new one:  
```docker
docker run --rm -it -v /srv/gitlab-runner/config:/etc/gitlab-runner gitlab/gitlab-runner register -n \
	--url https://gitlab.lrz.de/ \
  --registration-token zNPJy8fsw8yGc5qs1v-V \
  --executor docker \
  --description "gitlab-runner-ke" \
  --docker-image "docker:19.03.12" \
  --docker-volumes /var/run/docker.sock:/var/run/docker.sock
```
We use the the gitlab-runner as a docker container way to use the gitlab-runner, and use the docker socket binding way to run docker command in the docker containner.  
The info about url and registration-token could be found in the setting->ci/cd->runner, and recommend to reset the token very time before you copy the token to register a new runner.  


<br>

Current webiste address:  http://3.71.93.212:3000/ (Latest check in 11/02/2022 at 12pm)  
How to log in to our website:  
|  TestAccounts   | Username | Password  |
|  ----  | ----  |----  |
| Customer  | CUSTOMER |CUSTOMER |
| Deliverer | DELIVERER |DELIVERER |
| Dispatcher | DISPATHCER | DISPATCHER |

<br>

HardwareTestAccounts:
|  TestAccounts   | Username | Password  |
|  ----  | ----  |----  |
| Customer  | HardwareTestCustomer |HardwareTestCustomer |
| Deliverer | HardwareTestDeliverer |HardwareTestDeliverer |
| Box | HardwareTestBox | HardwareTestBox |

<br>

HardwareInfo:  
Box_id: ```61eedcdb818c905a54c2128a```   
HardwareTestCustomer rfid (blue card): ```xBTQpTImrgZgg6Kh```      
HardwareTestDeliverer rfid (white card): ```wxe9yZEZraa9C1ad```

<br>

Related projects:  
[ASE-Delivery-Hardware](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-hardware)  
[ASE-Delivery-SPA](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-spa)  
[ASE-Delivery-Discovery-Server](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-discovery-server)  
[ASE-Delivery-CAS](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-cas)  
[ASE-Delivery-DSE](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-dse)  
[ASE-Delivery-Gateway](https://gitlab.lrz.de/ase-21-22/team-8/ase-delivery-gateway)  

<br>

[Sample vedio link](https://youtu.be/wexj4nhLz5I)  
And sorry for the poor audio quality, Our audio in the CI/CD part was not recorded by accident, and we choose not to re-record it... But we have the script of that part:  
>And for the Ci/Cd part, we’ve established the ci/cd pipeline, now every commit in the specific directories will trigger specific jobs

>Now let us do some change in the spa directories, 
we uncomment this, to add a line under the login panel, and we commit it and push to the remote repository

>and now we could see the new pipeline in the gitlab website, we could see the jobs related to the spa project running, 

>Now we could go to the setting->ci/cd->variables to see the ci variables, the ci_registry_username and ci_password are used to let our local gitlab runner and aws server to connect to the gitlab and push or pull the images to or from the gitlab 
And in the runner, we could see a runner is active

>Now the pipeline is finished, we could see they are all green in the gitlab pipeline page. And in the packag&registry we could see the new image we just published

>And now we could go to our VM to see the docker container, we could see the container just started a minute ago, and we enter the url in the browser, then we could see the line we just uncommented showing in the page

