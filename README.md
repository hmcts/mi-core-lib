# Management Information Core

### Core libraries and utilities for the MI services

Library for Azure authentication based on entered details.


Re-tag usage:

Name of the ACR is the one in use.
Repository is the parent level container.
Tag is the tag to update.

The new tag will automatically be put into the same ACR and Repository but with a new datetime.

Therefore this pipeline assumes the tag is in the following format:

{env}-{docker_tag}-{create_datetime}

and the {create_datetime} part will be replace by the current datetime in +%Y%m%d%H%M%S format.