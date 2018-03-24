<a name=""></a>
#  (2018-03-24)



<a name="0.0.4-release"></a>
## [0.0.4-release](https://git.cs.usask.ca/CMPT371T3/Auxilium/compare/0.0.3-release...0.0.4-release) (2018-03-18)


### Bug Fixes

* **build.gradle:** comment out an line to try to pass the pipeline ([50bcdd1](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/50bcdd1))
* removal of inexistant profile activity from manifest ([dc8dc89](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/dc8dc89))
* **HelpScreen.java:** properly check if condition string is null ([e8e2e59](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/e8e2e59))
* **ProfileEditActivity:** user can no longer leave the fields with an asterix beside them blank ([34d050a](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/34d050a))
* remove fab from chat screens ([9a14bfe](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/9a14bfe))
* user sent to main activity if they already have an account when signing in ([15c2181](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/15c2181))


### Features

* registration page and proper navigation from signing in to the registration page to the main activity ([91838b2](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/91838b2))
* sign out option in the options menu in main activity ([dceaa69](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/dceaa69))
* **navigation:** populate drawer with chats, helpscreen.  Helpscreen populate using index diagnosis ([74fcbdc](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/74fcbdc))



<a name="0.0.3-release"></a>
## [0.0.3-release](https://git.cs.usask.ca/CMPT371T3/Auxilium/compare/0.0.2-release...0.0.3-release) (2018-03-04)


### Bug Fixes

* **build.gradle:** comment out an line to try to pass the pipeline ([e5a6475](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/e5a6475))
* **ci:** correct invalid yaml syntax ([369e61d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/369e61d))
* **gradlew:** changing file permissions ([1deaf05](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/1deaf05))
* **MainActivity.java:** add missing brace ([1ae837c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/1ae837c))
* **MainActivity.java:** remove nav_send from MainActivity -Symbol not ([dea8609](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/dea8609))
* **profile_page.xml:** add constraints to concerned party profiles ([eb9c63e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/eb9c63e))
* **profile_page.xml:** remove unnecessary code from pervious merge ([be9e130](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/be9e130))
* able to press buttons when messages flow off the screen on the index chat, chats also now scroll down to the newest message automatically ([b529902](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/b529902))
* IndexProfileActivity activity declaration in the manifest ([3d35611](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/3d35611))
* populate first and last name with display name instead of preferred name with the data ([3f4ae04](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/3f4ae04))
* User fields shown in profile and the profile editor no longer can be null. TextViews now assigned correctly in profile activity ([6c5288e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/6c5288e))


### Features

* add drawer layout of circles feature ([9436849](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/9436849))
* enable the create button on create room page to populate firebase database ([f4301fa](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/f4301fa))
* enable the create button on create room page to populate firebase database ([292e3cc](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/292e3cc))
* **ailmentSpinner:** add an ailment drop down, send selected ailment to Firebase database ([c3ca62e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/c3ca62e))
* **ailmentSpinner:** add an ailment drop down, send selected ailment to Firebase database ([4433a4d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/4433a4d))
* **ailmentSpinner:** add an ailment drop down, send selected ailment to Firebase database ([fdddf65](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/fdddf65))
* **ailmentSpinner:** add an ailment drop down, send selected ailment to Firebase database ([b4ee37e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/b4ee37e))
* **ci:** add deploy stage that uploads apk to gitlab ([8376f94](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/8376f94)), closes [#27](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/27)
* **ci:** add distinct build stage and add findbugs as part of analysis stage ([26c3c48](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/26c3c48)), closes [#26](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/26)
* new user data in User.java ([fa9c622](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/fa9c622))
* **ci:** add when clause to lint state ([a01dd36](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/a01dd36)), closes [#28](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/28)
* add ability to send invitations ([3266fbe](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/3266fbe)), closes [#49](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/49)
* add basic configuration for change log tool ([92d02b6](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/92d02b6))
* add joda-time as dependency ([a474c55](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/a474c55))
* **IndexChat.java:** add basic index group chat ([714d1e8](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/714d1e8))
* add stage to generate test coverage report ([e05d577](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/e05d577)), closes [#38](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/38)
* add static code analysis as a gradle task ([a63888d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/a63888d)), closes [#26](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/26)
* add task to generate test coverage report ([7615bc2](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/7615bc2)), closes [#38](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/38)
* feedback to user when saving changes to profile data ([c32f75c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/c32f75c))
* include exclude file for findbugs task ([be71dc4](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/be71dc4))
* **ci:** include testing stage for pipeline ([891ce91](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/891ce91)), closes [#37](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/37)
* introduce logic to create and insert invitations ([d8f300e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/d8f300e)), closes [#49](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/49)
* make build fail if checkstyle related errors occur ([5fb69a8](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/5fb69a8)), closes [#25](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/25)
* menu for navigation from profile page to profile edit page ([4da931b](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/4da931b))
* navigation to the main activity from the signin activity ([5113393](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/5113393))
* profile activity class and data population ([9e62bd1](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/9e62bd1))
* **HelpScreen.java:** populate help screen with resources from database ([c73ee53](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/c73ee53))
* profile page changed to a fragment that is accessible from the nav bar and options menu ([5909539](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/5909539))
* **ci:** cache gradle and build artifacts ([b7bdf23](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/b7bdf23))
* **ci:** cache on a per job basis ([279556c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/279556c))
* **DirectChat:** 1:1 private chat ([f8ada95](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/f8ada95))
* **drawer1:** code for proper linking of screens ([9896649](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/9896649))
* **drawer1:** correct screens now properly linked to correct buttons ([bb04e2e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/bb04e2e))
* **groupchat:** add groupchat functionality ([fb0578c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/fb0578c))
* **profile_page.xml:** profile page for the concerned members ([1cfb1b1](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/1cfb1b1))
* **welcomeJoinCreate:** create welcome activity ([987a5b7](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/987a5b7))
* rename apk file as part of the deploy stage ([4c47164](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/4c47164))
* saving/loading data from db on the profile and edit profile screens ([1930367](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/1930367))
* upload test reports on failure ([ec8bf03](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/ec8bf03))


### Performance Improvements

* deadstore and unused variable removal ([0d1d07c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/0d1d07c))
* unread variable removal ([8a05016](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/8a05016))
* unread variable removal ([487672c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/487672c))



<a name="0.0.1-release"></a>
## [0.0.1-release](https://git.cs.usask.ca/CMPT371T3/Auxilium/compare/ed0da89...0.0.1-release) (2018-02-04)


* feat ([9e2e635](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/9e2e635))
* feat ([5699343](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/5699343))


### Bug Fixes

* **createRoom:** add constraints to GUI elements ([d912d30](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/d912d30))
* **createRoom:** add constraints to GUI elements ([095ccb9](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/095ccb9))


### Features

* add Google login ([1e2ae48](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/1e2ae48))
* add Google login ([b566b71](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/b566b71))
* **.gitignore:** add inital gitignore file for andriod projects ([ed0da89](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/ed0da89))
* **checkstyle:** add checkstyle task ([a85a7cf](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/a85a7cf))
* **checkstyle:** add checkstyle task ([cc90169](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/cc90169))
* **checkstyle:** include google's java checkstyle.xml ([664c0b1](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/664c0b1))
* **checkstyle:** include google's java checkstyle.xml ([fde3dae](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/fde3dae))
* **ci:** add clause to expire artifacts ([a068342](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/a068342))
* **ci:** add pipeline that lints/checktyles code ([0034100](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/0034100)), closes [#1](https://git.cs.usask.ca/CMPT371T3/Auxilium/issues/1)
* add screen ([21c0790](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/21c0790))
* add screen ([7076659](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/7076659))
* **screen:** add group chat screen ([26924e4](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/26924e4))
* create basic layout of join page ([8049a7d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/8049a7d))
* create basic layout of join page ([011a6f8](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/011a6f8))
* create basic layout of join page ([bc4244d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/bc4244d))
* create basic layout of join page ([686b246](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/686b246))
* **welcomeJoinCreate:** create welcome activity ([6cad7fe](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/6cad7fe))
* imageview for a profile picture to index_profile_page.xml ([0fe56ef](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/0fe56ef))
* **CONTRIBUTING.md:** add contributing documentation with basic guidelines ([eb6cc44](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/eb6cc44))
* **createRoom:** create an interface for user to register/create a group ([b6168cf](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/b6168cf))
* **createRoom:** create an interface for user to register/create a group ([d9fcdbe](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/d9fcdbe))
* **createScreen:** add elements of create screen ([c9251c6](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/c9251c6))
* **createScreen:** add elements of create screen ([3cbc662](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/3cbc662))
* **join:** setting up Join feat ([030fde0](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/030fde0))
* **join:** setting up Join feat ([68fe626](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/68fe626))
* **join:** setting up Join feat ([f3eb8ea](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/f3eb8ea))
* **join:** setting up Join feat ([143d443](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/143d443))
* **README.md:** add readme with basic project description ([aa5a96e](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/aa5a96e))
* **screen:** add settings screen ([e2e170d](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/e2e170d))
* **screen:** add settings screen ([37d5927](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/37d5927))
* **welcomeJoinCreate:** create welcome activity ([106f8b2](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/106f8b2))
* **welcomeJoinCreate:** create welcome activity ([7b6b77b](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/7b6b77b))
* **welcomeJoinCreate:** create welcome activity ([34b698b](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/34b698b))
* **welcomeJoinCreate:** create welcome activity ([34f9682](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/34f9682))
* **welcomeJoinCreate:** create welcome activity ([2a97c3b](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/2a97c3b))
* **welcomeJoinCreate:** create welcome activity ([c0dcf8c](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/c0dcf8c))
* **welcomeJoinCreate:** create welcome activity ([d2b5647](https://git.cs.usask.ca/CMPT371T3/Auxilium/commits/d2b5647))


### BREAKING CHANGES

* 
* 
* 
* 



