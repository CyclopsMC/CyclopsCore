## CommonCapabilitiesAPI

API for the [CommonCapabilities](https://github.com/CyclopsMC/CommonCapabilities) mod.
This API simply contains various capabilities that can be used by any mod.

### Contributing
* New Capabilities should first be discussed through an RFC issue.
* All Capabilities must have a default implementation that acts as an example for other modders.
* All code must comply to our coding conventions, be clean and must be well documented.

### Using the API

Don't package the API (*recommended*):
```
git submodule add https://github.com/CyclopsMC/CommonCapabilitiesAPI.git src/api/java/org/cyclops/commoncapabilities/api/
```

Repackage the API (Only if you know what you're doing):
```
git submodule add https://github.com/CyclopsMC/CommonCapabilitiesAPI.git src/main/java/org/cyclops/commoncapabilities/api/
```

### Branching Strategy

For every major Minecraft version, two branches exist:

* `master-{mc_version}`: Latest (potentially unstable) development.
* `release-{mc_version}`: Latest stable release for that Minecraft version. This is also tagged with all mod releases.

### License
All code and images are licenced under the [MIT License](https://github.com/CyclopsMC/CommonCapabilitiesAPI/blob/master-1.8/LICENSE.txt)
